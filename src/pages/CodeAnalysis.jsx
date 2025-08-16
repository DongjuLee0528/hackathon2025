import React, { useState, useRef, useCallback, useMemo } from "react";
import styled from "styled-components";
import CodeEditor from "../components/CodeEditor";

const Container = styled.div`
  height: 100vh;
  min-height: 1010px;
  position: relative;
  background: #efefef;
  overflow: hidden;
`;

const Frame = styled.div`
  width: 810px;
  height: 860px;
  position: absolute;
  top: 75px;
  left: 137px;
  background: white;
  box-shadow: 3px 3px 4px rgba(0, 0, 0, 0.3);
  border-top-left-radius: 60px;
  border-bottom-left-radius: 60px;
  padding: 35px 50px;
  box-sizing: border-box;
`;

const CodeAnalysisContainer = styled.div`
  width: 810px;
  height: 860px;
  position: absolute;
  top: 75px;
  left: 972px;
  background: white;
  box-shadow: 3px 3px 4px rgba(0, 0, 0, 0.3);
  border-top-right-radius: 60px;
  border-bottom-right-radius: 60px;
  padding: 35px 50px;
  overflow-y: auto;
  box-sizing: border-box;
`;

const ControlsContainer = styled.div`
  display: flex;
  gap: 15px;
  margin-bottom: 30px;
  align-items: center;
  flex-wrap: wrap;
`;

const SelectBox = styled.select`
  width: ${(props) => props.w || "150px"};
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
`;

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

const SuccessMessage = styled.div`
  padding: 15px;
  margin-bottom: 20px;
  border-radius: 10px;
  background-color: #d3f9d8;
  color: #2b8a3e;
  font-size: 16px;
  border-left: 4px solid #51cf66;
`;

const ResultList = styled.div`
  border: 2px solid #e9ecef;
  border-radius: 15px;
  padding: 25px 30px;
  background-color: #f8f9fa;
`;

const ResultItem = styled.div`
  display: flex;
  justify-content: space-between;
  padding: 10px 0;
  font-size: 18px;
  border-bottom: 1px solid #dee2e6;
  &:last-child {
    border-bottom: none;
  }
`;

const ResultLabel = styled.span`
  font-weight: 500;
`;

const ResultScore = styled.span`
  font-weight: 600;
  color: #37b24d;
`;

const LANGUAGE_TEMPLATES = {
  javascript: `function hello() {\n  console.log("Hello, JavaScript!");\n}`,
  python: `def hello():\n    print("Hello, Python!")`,
  java: `public class Main {\n    public static void main(String[] args) {\n        System.out.println("Hello, Java!");\n    }\n}`
};

const ALLOWED_EXTENSIONS = [
  ".js", ".jsx", ".ts", ".tsx", ".py", ".java", ".cpp", ".c", 
  ".go", ".php", ".html", ".css", ".json", ".txt"
];

const MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB

export default function CodeAnalysis() {
  const [language, setLanguage] = useState("javascript");
  const [code, setCode] = useState(LANGUAGE_TEMPLATES["javascript"]);
  const [fontSize, setFontSize] = useState(14);
  const [pageMode, setPageMode] = useState("text");
  const [fileList, setFileList] = useState([]);
  const [errorMessage, setErrorMessage] = useState("");
  const [successMessage, setSuccessMessage] = useState("");
  const [results, setResults] = useState([]);
  const fileInputRef = useRef(null);

  const isAnalyzeDisabled = useMemo(() => {
    if (pageMode === "text") return !code.trim();
    return fileList.length === 0;
  }, [pageMode, code, fileList.length]);

  const handleAnalyzeClick = () => {
    setResults([
      { label: "코드 품질", score: "84/100" },
      { label: "보안성", score: "79/100" },
      { label: "성능", score: "67/100" },
      { label: "가독성", score: "66/100" },
      { label: "유지보수성", score: "92/100" }
    ]);
    setSuccessMessage("코드 분석이 완료되었습니다!");
  };

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

  // 페이지 모드 변경 핸들러
  const handlePageModeChange = useCallback((e) => {
    const selectPageMode = e.target.value;
    setPageMode(selectPageMode);
    setErrorMessage("");
    setSuccessMessage("");
    setResults([]);
    
    if (selectPageMode === "text") {
      resetForm();
    }
  }, [resetForm]);

  return (
    <Container>
      <Frame>
        <ControlsContainer>
          <SelectBox value={pageMode} onChange={handlePageModeChange}>
            <option value="text">텍스트 입력</option>
            <option value="zip">파일 업로드</option>
          </SelectBox>

          <SelectBox
            value={language}
            onChange={(e) => {
              setLanguage(e.target.value);
              setCode(LANGUAGE_TEMPLATES[e.target.value]);
            }}
            disabled={pageMode === "zip"}
          >
            <option value="javascript">JavaScript</option>
            <option value="python">Python</option>
            <option value="java">Java</option>
          </SelectBox>

          <SelectBox
            w="100px"
            value={fontSize}
            onChange={(e) => setFontSize(parseInt(e.target.value))}
            disabled={pageMode === "zip"}
          >
            <option value={12}>12px</option>
            <option value={14}>14px</option>
            <option value={16}>16px</option>
          </SelectBox>

          <AnalyzeBtn onClick={handleAnalyzeClick} disabled={isAnalyzeDisabled}>
            코드분석
          </AnalyzeBtn>
        </ControlsContainer>

        {errorMessage && <ErrorMessage>{errorMessage}</ErrorMessage>}
        {successMessage && <SuccessMessage>{successMessage}</SuccessMessage>}

        {pageMode === "text" ? (
          <CodeEditor
            code={code}
            setCode={setCode}
            language={language}
            fontSize={fontSize}
          />
        ) : (
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
        <h2>코드 분석 결과</h2>
        {results.length > 0 && (
          <ResultList>
            {results.map((r, i) => (
              <ResultItem key={i}>
                <ResultLabel>{r.label}</ResultLabel>
                <ResultScore>{r.score}</ResultScore>
              </ResultItem>
            ))}
          </ResultList>
        )}
      </CodeAnalysisContainer>
    </Container>
  );
}