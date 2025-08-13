import React, { useEffect, useRef, useState, useMemo } from "react";
import styled from "styled-components";

// CodeMirror는 Claude 환경에서 직접 사용할 수 없으므로 간단한 텍스트 에디터로 구현
const EditorContainer = styled.div`
  width: 100%;
  height: 100%;
  border-radius: 8px;
  overflow: hidden;
  background-color: #2d2d2d;
  position: relative;
`;

const WarningPanel = styled.div`
  padding: 12px 15px;
  margin-bottom: 10px;
  background-color: #fff3cd;
  border-left: 4px solid #ffc107;
  color: #856404;
  font-size: 0.85rem;
  border-radius: 4px;
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  z-index: 10;

  h4 {
    margin: 0 0 8px 0;
    font-size: 0.9rem;
    font-weight: 600;
  }

  ul {
    margin: 0;
    padding-left: 20px;
  }

  li {
    margin-bottom: 4px;
  }
`;

const StyledTextArea = styled.textarea`
  width: 100%;
  height: 100%;
  background-color: #2d2d2d;
  color: #d4d4d4;
  border: none;
  resize: none;
  outline: none;
  padding: ${props => props.hasWarning ? '80px 20px 20px 20px' : '20px'};
  font-family: 'Fira Code', 'Monaco', 'Menlo', 'Consolas', monospace;
  font-size: ${props => props.fontSize}px;
  line-height: 1.5;
  tab-size: 2;
  box-sizing: border-box;

  &::selection {
    background-color: #264f78;
  }

  /* 스크롤바 스타일링 */
  &::-webkit-scrollbar {
    width: 14px;
  }

  &::-webkit-scrollbar-track {
    background: #1e1e1e;
  }

  &::-webkit-scrollbar-thumb {
    background: #424242;
    border-radius: 6px;
  }

  &::-webkit-scrollbar-thumb:hover {
    background: #4f4f4f;
  }
`;

const LineNumbers = styled.div`
  position: absolute;
  left: 0;
  top: ${props => props.hasWarning ? '80px' : '0'};
  bottom: 0;
  width: 50px;
  background-color: #1e1e1e;
  color: #858585;
  font-family: 'Fira Code', 'Monaco', 'Menlo', 'Consolas', monospace;
  font-size: ${props => props.fontSize}px;
  line-height: 1.5;
  padding: 20px 10px;
  box-sizing: border-box;
  overflow: hidden;
  user-select: none;
  border-right: 1px solid #3c3c3c;
`;

const EditorWrapper = styled.div`
  position: relative;
  width: 100%;
  height: 100%;
  display: flex;
`;

const TextAreaWrapper = styled.div`
  flex: 1;
  position: relative;
`;

// 자동완성 관련 스타일
const AutocompleteContainer = styled.div`
  position: absolute;
  background: #252526;
  border: 1px solid #3c3c3c;
  border-radius: 4px;
  max-height: 200px;
  overflow-y: auto;
  z-index: 1000;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.3);
  min-width: 200px;
`;

const AutocompleteItem = styled.div`
  padding: 8px 12px;
  color: #d4d4d4;
  cursor: pointer;
  font-family: 'Fira Code', 'Monaco', 'Menlo', 'Consolas', monospace;
  font-size: 14px;
  border-bottom: 1px solid #3c3c3c;

  &:hover, &.selected {
    background-color: #094771;
  }

  &:last-child {
    border-bottom: none;
  }
`;

// 언어별 키워드 및 자동완성 제안
const LANGUAGE_KEYWORDS = {
  javascript: [
    'function', 'const', 'let', 'var', 'if', 'else', 'for', 'while', 'return', 
    'class', 'extends', 'import', 'export', 'async', 'await', 'try', 'catch',
    'console.log', 'console.error', 'console.warn', 'JSON.stringify', 'JSON.parse'
  ],
  typescript: [
    'function', 'const', 'let', 'var', 'if', 'else', 'for', 'while', 'return',
    'class', 'extends', 'import', 'export', 'async', 'await', 'try', 'catch',
    'interface', 'type', 'enum', 'public', 'private', 'protected', 'readonly'
  ],
  python: [
    'def', 'class', 'if', 'elif', 'else', 'for', 'while', 'return', 'import',
    'from', 'try', 'except', 'finally', 'with', 'as', 'lambda', 'yield',
    'print()', '__init__', 'self', 'super()', 'len()', 'range()', 'enumerate()'
  ],
  java: [
    'public', 'private', 'protected', 'class', 'interface', 'extends', 'implements',
    'static', 'final', 'abstract', 'if', 'else', 'for', 'while', 'return',
    'System.out.println', 'String', 'int', 'void', 'boolean', 'try', 'catch'
  ],
  c: [
    '#include', 'int', 'char', 'float', 'double', 'void', 'struct', 'union',
    'if', 'else', 'for', 'while', 'return', 'sizeof', 'malloc', 'free',
    'printf', 'scanf', 'main', 'NULL', 'typedef', 'const', 'static'
  ],
  cpp: [
    '#include', 'int', 'char', 'float', 'double', 'void', 'class', 'struct',
    'if', 'else', 'for', 'while', 'return', 'namespace', 'using', 'std::',
    'cout', 'cin', 'endl', 'vector', 'string', 'public', 'private', 'protected'
  ]
};

const CodeEditor = ({ 
  code, 
  setCode, 
  language = "javascript", 
  fontSize = 14,
  placeholder = "코드를 입력하세요..." 
}) => {
  const textAreaRef = useRef(null);
  const [lines, setLines] = useState(1);
  const [isOverflowing, setIsOverflowing] = useState(false);
  const [showAutocomplete, setShowAutocomplete] = useState(false);
  const [autocompletePosition, setAutocompletePosition] = useState({ top: 0, left: 0 });
  const [filteredSuggestions, setFilteredSuggestions] = useState([]);
  const [selectedSuggestionIndex, setSelectedSuggestionIndex] = useState(0);
  const [currentWord, setCurrentWord] = useState('');

  // 라인 번호 생성
  const lineNumbers = useMemo(() => {
    const lineCount = Math.max(lines, 20);
    return Array.from({ length: lineCount }, (_, i) => i + 1);
  }, [lines]);

  // 코드 변경 핸들러
  const handleCodeChange = (e) => {
    const newCode = e.target.value;
    setCode(newCode);
    
    // 라인 수 계산
    const newLines = newCode.split('\n').length;
    setLines(newLines);
    setIsOverflowing(newLines > 30);

    // 자동완성 처리
    handleAutocomplete(e);
  };

  // 자동완성 로직
  const handleAutocomplete = (e) => {
    const textarea = e.target;
    const cursorPos = textarea.selectionStart;
    const textBeforeCursor = textarea.value.substring(0, cursorPos);
    const words = textBeforeCursor.split(/\s+/);
    const currentWordMatch = textBeforeCursor.match(/(\w+)$/);
    
    if (currentWordMatch && currentWordMatch[1].length > 0) {
      const word = currentWordMatch[1];
      setCurrentWord(word);
      
      const keywords = LANGUAGE_KEYWORDS[language] || LANGUAGE_KEYWORDS.javascript;
      const filtered = keywords.filter(keyword => 
        keyword.toLowerCase().startsWith(word.toLowerCase())
      );
      
      if (filtered.length > 0) {
        setFilteredSuggestions(filtered);
        setSelectedSuggestionIndex(0);
        setShowAutocomplete(true);
        
        // 자동완성 위치 계산 (간단한 근사치)
        const rect = textarea.getBoundingClientRect();
        const lineHeight = fontSize * 1.5;
        const lines = textBeforeCursor.split('\n');
        const currentLine = lines.length - 1;
        const currentColumn = lines[lines.length - 1].length;
        
        setAutocompletePosition({
          top: rect.top + (currentLine * lineHeight) + lineHeight + 5,
          left: rect.left + 50 + (currentColumn * fontSize * 0.6)
        });
      } else {
        setShowAutocomplete(false);
      }
    } else {
      setShowAutocomplete(false);
    }
  };

  // 키보드 이벤트 핸들러
  const handleKeyDown = (e) => {
    if (showAutocomplete) {
      if (e.key === 'ArrowDown') {
        e.preventDefault();
        setSelectedSuggestionIndex(prev => 
          Math.min(prev + 1, filteredSuggestions.length - 1)
        );
      } else if (e.key === 'ArrowUp') {
        e.preventDefault();
        setSelectedSuggestionIndex(prev => Math.max(prev - 1, 0));
      } else if (e.key === 'Tab' || e.key === 'Enter') {
        e.preventDefault();
        insertSuggestion(filteredSuggestions[selectedSuggestionIndex]);
      } else if (e.key === 'Escape') {
        setShowAutocomplete(false);
      }
    } else if (e.key === 'Tab') {
      e.preventDefault();
      const start = e.target.selectionStart;
      const end = e.target.selectionEnd;
      const newCode = code.substring(0, start) + '  ' + code.substring(end);
      setCode(newCode);
      
      setTimeout(() => {
        e.target.selectionStart = e.target.selectionEnd = start + 2;
      }, 0);
    }
  };

  // 자동완성 삽입
  const insertSuggestion = (suggestion) => {
    const textarea = textAreaRef.current;
    const cursorPos = textarea.selectionStart;
    const textBeforeCursor = textarea.value.substring(0, cursorPos);
    const textAfterCursor = textarea.value.substring(cursorPos);
    
    const wordStartPos = textBeforeCursor.lastIndexOf(currentWord);
    const newText = textBeforeCursor.substring(0, wordStartPos) + suggestion + textAfterCursor;
    
    setCode(newText);
    setShowAutocomplete(false);
    
    setTimeout(() => {
      const newCursorPos = wordStartPos + suggestion.length;
      textarea.selectionStart = textarea.selectionEnd = newCursorPos;
      textarea.focus();
    }, 0);
  };

  // 자동완성 아이템 클릭
  const handleAutocompleteClick = (suggestion) => {
    insertSuggestion(suggestion);
  };

  // 외부 클릭 시 자동완성 숨기기
  useEffect(() => {
    const handleClickOutside = () => {
      setShowAutocomplete(false);
    };

    if (showAutocomplete) {
      document.addEventListener('click', handleClickOutside);
      return () => document.removeEventListener('click', handleClickOutside);
    }
  }, [showAutocomplete]);

  return (
    <EditorContainer>
      {isOverflowing && (
        <WarningPanel>
          <h4>⚠️ 성능 경고</h4>
          <ul>
            <li>파일이 너무 큽니다 ({lines}줄)</li>
            <li>편집기 성능이 저하될 수 있습니다</li>
          </ul>
        </WarningPanel>
      )}

      <EditorWrapper>
        <LineNumbers fontSize={fontSize} hasWarning={isOverflowing}>
          {lineNumbers.map(num => (
            <div key={num}>{num}</div>
          ))}
        </LineNumbers>

        <TextAreaWrapper>
          <StyledTextArea
            ref={textAreaRef}
            value={code}
            onChange={handleCodeChange}
            onKeyDown={handleKeyDown}
            fontSize={fontSize}
            hasWarning={isOverflowing}
            placeholder={placeholder}
            spellCheck={false}
            autoCapitalize="off"
            autoComplete="off"
          />
        </TextAreaWrapper>
      </EditorWrapper>

      {showAutocomplete && (
        <AutocompleteContainer
          style={{
            position: 'fixed',
            top: autocompletePosition.top,
            left: autocompletePosition.left
          }}
        >
          {filteredSuggestions.map((suggestion, index) => (
            <AutocompleteItem
              key={suggestion}
              className={index === selectedSuggestionIndex ? 'selected' : ''}
              onClick={() => handleAutocompleteClick(suggestion)}
            >
              {suggestion}
            </AutocompleteItem>
          ))}
        </AutocompleteContainer>
      )}
    </EditorContainer>
  );
};

export default CodeEditor;