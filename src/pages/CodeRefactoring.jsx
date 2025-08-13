import React, { useState, useRef, useCallback, useMemo } from "react";
import styled, { keyframes } from "styled-components";
import CodeEditor from '../components/CodeEditor'; // 위에서 만든 CodeEditor 컴포넌트 import

// 키프레임 애니메이션
const spin = keyframes`
  to {
    transform: rotate(360deg);
  }
`;

// 메인 컨테이너
const Container = styled.div`
  height: 100vh;
  min-height: 1010px;
  position: relative;
  background: #efefef;
  overflow: hidden;
`;

// 왼쪽 프레임 (코드 편집기 영역)
const Frame = styled.div`
  width: 810px;
  height: 860px;
  position: absolute;
  top: 75px;
  left: 137px;
  background: white;
  box-shadow: 3px 3px 4px rgba(0, 0, 0, 0.3);
  overflow: hidden;
  border-top-left-radius: 60px;
  border-bottom-left-radius: 60px;
  padding: 35px 50px;
  box-sizing: border-box;
`;

// 오른쪽 프레임 (분석 결과 영역)
const CodeAnalysisContainer = styled.div`
  width: 810px;
  height: 860px;
  position: absolute;
  top: 75px;
  left: 972px;
  background: white;
  box-shadow: 3px 3px 4px rgba(0, 0, 0, 0.3);
  overflow: hidden;
  border-top-right-radius: 60px;
  border-bottom-right-radius: 60px;
  padding: 35px 50px;
  overflow-y: auto;
  box-sizing: border-box;
`;

// 컨트롤 영역
const ControlsContainer = styled.div`
  display: flex;
  gap: 15px;
  margin-bottom: 30px;
  align-items: center;
  flex-wrap: wrap;
`;

// 셀렉트 박스들
const SelectBox = styled.select`
  width: 150px;
  height: 60px;
  background: white;
  border-radius: 15px;
  border: 3px solid #b8b8b8;
  font-family: Inter, sans-serif;
  font-size: 20px;
  font-weight: 400;
  color: black;
  text-align: center;
  cursor: pointer;
  outline: none;
  padding: 0 10px;

  &:focus {
    border-color: #4dabf7;
  }

  option {
    padding: 10px;
    font-size: 16px;
  }
`;

const ModeSelectBox = styled(SelectBox)`
  width: 150px;
`;

// 폰트 사이즈 셀렉트 박스
const FontSizeSelectBox = styled(SelectBox)`
  width: 100px;
`;

// 분석 버튼
const AnalyzeBtn = styled.button`
  width: 150px;
  height: 60px;
  background: #7de040;
  border-radius: 15px;
  border: none;
  font-family: Inter, sans-serif;
  font-size: 24px;
  font-weight: 400;
  color: black;
  cursor: pointer;
  transition: all 0.2s ease;

  &:hover:not(:disabled) {
    background: #6bc935;
    transform: translateY(-1px);
  }

  &:active:not(:disabled) {
    transform: translateY(0);
  }

  &:disabled {
    background: #adb5bd;
    cursor: not-allowed;
    opacity: 0.6;
  }
`;

// CodeMirror 에디터 컨테이너
const EditorContainer = styled.div`
  width: 100%;
  height: calc(100% - 120px);
  max-height: 600px;
  border-radius: 10px;
  overflow: hidden;
  background-color: #2d2d2d;
  box-sizing: border-box;
`;

// 파일 업로드 관련 스타일
const FileInputBox = styled.div`
  width: 100%;
  height: 400px;
  margin-bottom: 20px;
`;

const FileInput = styled.input`
  display: none;
`;

const FileInputLabel = styled.label`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
  font-size: 24px;
  color: #495057;
  cursor: pointer;
  border: 3px dashed #ced4da;
  border-radius: 15px;
  transition: all 0.3s ease-in-out;
  background-color: #f8f9fa;
  box-sizing: border-box;

  &:hover {
    border-color: #4dabf7;
    color: #4dabf7;
    background-color: #e3f2fd;
    transform: scale(1.02);
  }

  &:focus-within {
    border-color: #4dabf7;
    outline: 2px solid rgba(77, 171, 247, 0.2);
  }
`;

const FileInputHint = styled.span`
  font-size: 16px;
  margin-top: 10px;
  color: #868e96;
  text-align: center;
  line-height: 1.4;
`;

const FileListContainer = styled.div`
  margin-top: 20px;
  padding: 15px;
  border: 2px solid #e9ecef;
  border-radius: 15px;
  background-color: #f8f9fa;
`;

const FileList = styled.ul`
  list-style: none;
  padding: 0;
  margin: 10px 0;
  max-height: 150px;
  overflow-y: auto;
`;

const FileItem = styled.li`
  display: flex;
  justify-content: space-between;
  padding: 8px 0;
  border-bottom: 1px solid #e9ecef;
  font-size: 16px;

  &:last-child {
    border-bottom: none;
  }
`;

const FileName = styled.span`
  font-weight: 500;
  word-break: break-all;
  flex: 1;
`;

const FileSize = styled.span`
  color: #6c757d;
  margin-left: 10px;
  white-space: nowrap;
  font-size: 14px;
`;

const ResetBtn = styled.button`
  margin-top: 10px;
  padding: 8px 16px;
  font-size: 16px;
  color: #495057;
  background-color: #e9ecef;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.2s ease;

  &:hover {
    background-color: #ced4da;
    transform: translateY(-1px);
  }
`;

// 에러 메시지
const ErrorMessage = styled.div`
  padding: 15px;
  margin-bottom: 20px;
  border-radius: 10px;
  background-color: #ffe3e3;
  color: #c92a2a;
  font-size: 16px;
  border-left: 4px solid #ff6b6b;
  white-space: pre-line;
`;

// 성공 메시지
const SuccessMessage = styled.div`
  padding: 15px;
  margin-bottom: 20px;
  border-radius: 10px;
  background-color: #d3f9d8;
  color: #2b8a3e;
  font-size: 16px;
  border-left: 4px solid #51cf66;
`;

// 분석 결과 영역
const Title = styled.h2`
  color: black;
  font-family: Inter, sans-serif;
  font-weight: 700;
  font-size: 36px;
  margin: 0 0 20px 0;
`;

const Subtitle = styled.p`
  color: #737373;
  font-family: Inter, sans-serif;
  font-weight: 400;
  font-size: 24px;
  margin: 0;
  line-height: 1.5;
`;

const LoadingIndicator = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 300px;
`;

const Spinner = styled.div`
  width: 40px;
  height: 40px;
  border: 4px solid rgba(77, 171, 247, 0.2);
  border-radius: 50%;
  border-top-color: #4dabf7;
  animation: ${spin} 1s ease-in-out infinite;
  margin-bottom: 20px;
`;

const AnalysisResult = styled.div`
  background: #f8f9fa;
  border-radius: 15px;
  padding: 25px;
  border-left: 4px solid #4dabf7;
  
  pre {
    white-space: pre-wrap;
    font-size: 16px;
    line-height: 1.6;
    color: #333;
    margin: 0;
    font-family: 'Consolas', 'Monaco', 'Courier New', monospace;
  }
`;

// 차트 컨테이너
const ChartContainer = styled.div`
  margin: 30px 0;
  padding: 25px;
  background: white;
  border-radius: 15px;
  border: 2px solid #e9ecef;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
`;

// 상수 정의
const LANGUAGE_TEMPLATES = {
  javascript: `function hello() {\n  console.log("Hello, JavaScript!");\n}`,
  typescript: `function hello(): void {\n  console.log("Hello, TypeScript!");\n}`,
  python: `def hello():\n    print("Hello, Python!")`,
  java: `public class Main {\n    public static void main(String[] args) {\n        System.out.println("Hello, Java!");\n    }\n}`,
  c: `#include <stdio.h>\n\nint main() {\n    printf("Hello, C!\\n");\n    return 0;\n}`,
  cpp: `#include <iostream>\n\nint main() {\n    std::cout << "Hello, C++!" << std::endl;\n    return 0;\n}`,
  csharp: `using System;\nclass Program {\n  static void Main() {\n    Console.WriteLine("Hello, C#!");\n  }\n}`,
  go: `package main\nimport "fmt"\nfunc main() {\n    fmt.Println("Hello, Go!")\n}`,
  rust: `fn main() {\n    println!("Hello, Rust!");\n}`,
  kotlin: `fun main() {\n    println("Hello, Kotlin!")\n}`,
  swift: `import Foundation\nprint("Hello, Swift!")`,
  ruby: `puts "Hello, Ruby!"`,
  php: `<?php\necho "Hello, PHP!";\n?>`,
  sql: `SELECT 'Hello, SQL!' AS greeting;`,
  bash: `#!/bin/bash\necho "Hello, Bash!"`,
  r: `print("Hello, R!")`,
  html: `<!DOCTYPE html>\n<html>\n<head>\n  <title>Hello HTML</title>\n</head>\n<body>\n  <h1>Hello, HTML!</h1>\n</body>\n</html>`,
  css: `body {\n  background-color: #f0f0f0;\n  color: #333;\n}`,
  json: `{\n  "message": "Hello, JSON!"\n}`
};

const ALLOWED_EXTENSIONS = [
  ".js", ".jsx", ".ts", ".tsx", ".py", ".java", ".cpp", ".c", 
  ".go", ".php", ".html", ".css", ".json", ".txt"
];

const MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB

// 메인 컴포넌트
const CodeRefactoring = () => {
  const [language, setLanguage] = useState("javascript");
  const [code, setCode] = useState(LANGUAGE_TEMPLATES["javascript"]);
  const [fontSize, setFontSize] = useState(14);
  const [showChart, setShowChart] = useState(false);
  const [scores, setScores] = useState([0, 0, 0, 0, 0]);
  const [pageMode, setPageMode] = useState("text");
  const [fileList, setFileList] = useState([]);
  const [errorMessage, setErrorMessage] = useState("");
  const [successMessage, setSuccessMessage] = useState("");
  const [isLoading, setIsLoading] = useState(false);
  const [analysisResult, setAnalysisResult] = useState("");
  const fileInputRef = useRef(null);

  // 메모이제이션된 값들
  const isAnalyzeDisabled = useMemo(() => {
    if (isLoading) return true;
    if (pageMode === "text") return !code.trim();
    return fileList.length === 0;
  }, [isLoading, pageMode, code, fileList.length]);

  // 코드 분석 함수
  const calculateScores = useCallback(async (codeToAnalyze) => {
    try {
      setIsLoading(true);
      setErrorMessage("");
      
      // API 호출 시뮬레이션
      await new Promise(resolve => setTimeout(resolve, 2000));
      
      // 실제 환경에서는 여기서 실제 API 호출
      const mockResult = `분석 완료!

코드 품질 점수: 85/100

개선 제안사항:
1. 함수명을 더 구체적으로 변경하는 것을 추천합니다.
2. JSDoc 주석을 추가하여 함수의 목적을 명확히 하세요.
3. 에러 핸들링을 추가해보세요.

개선된 코드:
/**
 * 환영 메시지를 출력하는 함수
 */
function displayWelcomeMessage() {
  try {
    console.log("Hello, JavaScript!");
  } catch (error) {
    console.error("메시지 출력 중 오류:", error);
  }
}`;
      
      setAnalysisResult(mockResult);
      setSuccessMessage("코드 분석이 완료되었습니다!");
      
      return Array.from({ length: 5 }, () => Math.floor(Math.random() * 41) + 60);
    } catch (error) {
      console.error("코드 분석 중 오류 발생:", error);
      setErrorMessage("코드 분석 중 오류가 발생했습니다. 네트워크 연결을 확인하고 다시 시도해주세요.");
      return [0, 0, 0, 0, 0];
    } finally {
      setIsLoading(false);
    }
  }, []);

  // 파일명 정규화
  const sanitizeFileName = useCallback((fileName) => {
    return fileName.replace(/[^\w\s.-]/g, '_');
  }, []);

  // 파일 확장자 검증
  const validateFileExtension = useCallback((fileName) => {
    const ext = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
    return ALLOWED_EXTENSIONS.includes(ext);
  }, []);

  // 파일 크기 검증
  const validateFileSize = useCallback((fileSize) => {
    return fileSize <= MAX_FILE_SIZE;
  }, []);

  // 분석 시작 핸들러
  const handleAnalyzeClick = useCallback(async () => {
    try {
      setErrorMessage("");
      setSuccessMessage("");
      setAnalysisResult("");
      
      const codeToAnalyze = pageMode === "text" ? code : "File analysis mode";
      const newScores = await calculateScores(codeToAnalyze);
      setScores(newScores);
      setShowChart(true);
    } catch (error) {
      setErrorMessage("분석 중 예기치 못한 오류가 발생했습니다.");
      console.error("분석 오류:", error);
    }
  }, [pageMode, code, calculateScores]);

  // 파일 변경 핸들러
  const handleFileChange = useCallback((event) => {
    setErrorMessage("");
    setSuccessMessage("");
    const files = event.target.files;
    const validFiles = [];
    const errors = [];
    
    if (files.length === 0) return;

    Array.from(files).forEach(file => {
      const safeFileName = sanitizeFileName(file.name);
      
      if (!validateFileExtension(file.name)) {
        errors.push(`${safeFileName}: 허용되지 않는 파일 형식입니다.`);
        return;
      }
      
      if (!validateFileSize(file.size)) {
        errors.push(`${safeFileName}: 파일 크기는 5MB 이하여야 합니다.`);
        return;
      }
      
      validFiles.push({
        name: safeFileName,
        size: Math.round(file.size / 1024) + ' KB',
        type: file.type || 'unknown',
        originalFile: file
      });
    });
    
    if (errors.length > 0) {
      setErrorMessage(errors.join('\n'));
    }
    
    if (validFiles.length > 0) {
      setFileList(validFiles);
      setSuccessMessage(`${validFiles.length}개 파일이 성공적으로 업로드되었습니다.`);
    }
  }, [sanitizeFileName, validateFileExtension, validateFileSize]);

  // 폼 리셋 핸들러
  const resetForm = useCallback(() => {
    setFileList([]);
    setErrorMessage("");
    setSuccessMessage("");
    if (fileInputRef.current) {
      fileInputRef.current.value = '';
    }
  }, []);

  // 언어 변경 핸들러
  const handleLanguageChange = useCallback((e) => {
    const selectedLang = e.target.value;
    setLanguage(selectedLang);
    setCode(LANGUAGE_TEMPLATES[selectedLang]);
    setErrorMessage("");
    setAnalysisResult("");
    setShowChart(false);
  }, []);

  // 페이지 모드 변경 핸들러
  const handlePageModeChange = useCallback((e) => {
    const selectPageMode = e.target.value;
    setPageMode(selectPageMode);
    setErrorMessage("");
    setSuccessMessage("");
    setAnalysisResult("");
    setShowChart(false);
    
    if (selectPageMode === "text") {
      resetForm();
    }
  }, [resetForm]);

  // 폰트 사이즈 변경 핸들러
  const handleFontSizeChange = useCallback((e) => {
    setFontSize(parseInt(e.target.value));
  }, []);

  return (
    <Container>
      <Frame>
        <ControlsContainer>
          <ModeSelectBox
            value={pageMode}
            onChange={handlePageModeChange}
            aria-label="입력 모드 선택"
          >
            <option value="text">텍스트 입력</option>
            <option value="zip">파일 업로드</option>
          </ModeSelectBox>

          <SelectBox
            value={language}
            onChange={handleLanguageChange}
            aria-label="프로그래밍 언어 선택"
            disabled={pageMode === "zip"}
          >
            <option value="javascript">JavaScript</option>
            <option value="typescript">TypeScript</option>
            <option value="python">Python</option>
            <option value="java">Java</option>
            <option value="c">C</option>
            <option value="cpp">C++</option>
            <option value="csharp">C#</option>
            <option value="go">Go</option>
            <option value="rust">Rust</option>
            <option value="kotlin">Kotlin</option>
            <option value="swift">Swift</option>
            <option value="ruby">Ruby</option>
            <option value="php">PHP</option>
            <option value="sql">SQL</option>
            <option value="bash">Bash</option>
            <option value="r">R</option>
            <option value="html">HTML</option>
            <option value="css">CSS</option>
            <option value="json">JSON</option>
          </SelectBox>

          <FontSizeSelectBox
            value={fontSize}
            onChange={handleFontSizeChange}
            aria-label="폰트 사이즈 선택"
            disabled={pageMode === "zip"}
          >
            <option value={12}>12px</option>
            <option value={14}>14px</option>
            <option value={16}>16px</option>
            <option value={18}>18px</option>
            <option value={20}>20px</option>
          </FontSizeSelectBox>

          <AnalyzeBtn 
            onClick={handleAnalyzeClick}
            disabled={isAnalyzeDisabled}
            aria-label="코드 분석 시작"
          >
            {isLoading ? "분석 중..." : "코드개선"}
          </AnalyzeBtn>
        </ControlsContainer>

        {errorMessage && (
          <ErrorMessage role="alert">
            {errorMessage}
          </ErrorMessage>
        )}

        {successMessage && (
          <SuccessMessage role="status">
            {successMessage}
          </SuccessMessage>
        )}

        {pageMode === "text" && (
          <EditorContainer>
            <CodeEditor
              code={code}
              setCode={setCode}
              language={language}
              fontSize={fontSize}
              placeholder={`${language.toUpperCase()} 코드를 입력하세요...`}
            />
          </EditorContainer>
        )}

        {pageMode === "zip" && (
          <div>
            <FileInputBox>
              <FileInput
                ref={fileInputRef}
                type="file"
                name="userCodingFile"
                id="fileInput"
                multiple
                onChange={handleFileChange}
                accept={ALLOWED_EXTENSIONS.join(',')}
                aria-label="파일 업로드"
              />
              <FileInputLabel htmlFor="fileInput">
                클릭하여 파일을 추가해주세요<br />
                <FileInputHint>
                  ({ALLOWED_EXTENSIONS.join(', ')} 파일, 최대 5MB)
                </FileInputHint>
              </FileInputLabel>
            </FileInputBox>
            
            {fileList.length > 0 && (
              <FileListContainer>
                <h3>선택된 파일 목록 ({fileList.length}개):</h3>
                <FileList>
                  {fileList.map((file, index) => (
                    <FileItem key={`${file.name}-${index}`}>
                      <FileName title={file.name}>{file.name}</FileName>
                      <FileSize>{file.size}</FileSize>
                    </FileItem>
                  ))}
                </FileList>
                <ResetBtn 
                  type="button" 
                  onClick={resetForm}
                  aria-label="파일 목록 초기화"
                >
                  초기화
                </ResetBtn>
              </FileListContainer>
            )}
          </div>
        )}
      </Frame>

      <CodeAnalysisContainer>
        <Title>코드 개선 결과</Title>
        {isLoading ? (
          <LoadingIndicator>
            <Spinner />
            <p>코드를 분석하고 있습니다...</p>
          </LoadingIndicator>
        ) : analysisResult ? (
          <>
            <AnalysisResult>
              <pre>{analysisResult}</pre>
            </AnalysisResult>
            {showChart && (
              <ChartContainer>
                <h3>코드 품질 점수</h3>
                <div style={{ display: 'flex', flexDirection: 'column', gap: '10px' }}>
                  <div>가독성: {scores[0]}/100</div>
                  <div>성능: {scores[1]}/100</div>
                  <div>보안: {scores[2]}/100</div>
                  <div>유지보수성: {scores[3]}/100</div>
                  <div>전체 점수: {scores[4]}/100</div>
                </div>
              </ChartContainer>
            )}
          </>
        ) : (
          <Subtitle>
            아직 개선 결과가 없습니다.
            <br />
            코드를 작성하거나 파일을 업로드한 후 '코드개선' 버튼을 클릭하세요.
          </Subtitle>
        )}
      </CodeAnalysisContainer>
    </Container>
  );
};

export default CodeRefactoring;