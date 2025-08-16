import React, { useState, useRef, useCallback, useMemo } from "react";
import styled from "styled-components";
import CodeEditor  from "../components/CodeEditor"; // 기존 CodeEditor 컴포넌트 재사용

// ---------- 레이아웃 컴포넌트 ----------
const StyledCodeProblem = styled.div`
  width: 1920px;
  height: 1010px;
  position: relative;
  background: #efefef;
  overflow: hidden;
`;

const StyledMainContainer = styled.div`
  width: 1646px;
  height: 860px;
  position: absolute;
  left: 137px;
  top: 75px;
  background: white;
  box-shadow: 0px 4px 4px rgba(0, 0, 0, 0.25);
  border-radius: 60px;
`;

const StyledProblemTitle = styled.div`
  position: absolute;
  left: 180px;
  top: 240px;
  font-size: 50px;
  font-family: Pretendard;
  font-weight: 600;
  color: black;
  word-wrap: break-word;
`;

const StyledProblemContent = styled.div`
  position: absolute;
  left: 180px;
  top: 340px;
  width: 750px;
  font-size: 18px;
  font-family: Pretendard;
  font-weight: 400;
  line-height: 1.6;
  color: black;
  word-wrap: break-word;
`;

const StyledDropdown = styled.select`
  width: 150px;
  height: 60px;
  position: absolute;
  background: white;
  border-radius: 15px;
  border: 3px solid #b8b8b8;
  color: black;
  font-size: 14px;
  font-family: Pretendard;
  font-weight: 400;
  padding: 0 10px;
  cursor: pointer;
  text-align: center;
`;

// 위치별로 재사용
const DropdownLanguage = styled(StyledDropdown)`
  left: 1000px;
  top: 130px;
`;
const DropdownProblemType = styled(StyledDropdown)`
  left: 1170px;
  top: 130px;
`;
const DropdownDifficulty = styled(StyledDropdown)`
  left: 1340px;
  top: 130px;
`;

const StyledButton = styled.button`
  width: 150px;
  height: 60px;
  position: absolute;
  background: #7de040;
  border-radius: 15px;
  border: 3px solid #b8b8b8;
  color: black;
  font-size: 18px;
  font-family: Pretendard;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s ease;

  &:hover {
    background: #6bc635;
    transform: scale(1.02);
  }
  &:active {
    background: #5ab025;
  }
`;

const StyledCodeEditorWrapper = styled.div`
  width: 680px;
  height: 350px;
  position: absolute;
  left: 1000px;
  top: 240px;
  background: #42414b;
  border-radius: 20px;
  overflow: hidden;
  box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.15);
`;

const StyledResultDisplay = styled.div`
  position: absolute;
  left: 180px;
  top: 820px;
  color: #2e8b57;
  font-size: 72px;
  font-family: Inter;
  font-weight: 700;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.1);
`;

const StyledErrorMessage = styled.div`
  position: absolute;
  left: 1000px;
  top: 195px;
  color: #ff4444;
  font-size: 14px;
  font-family: Pretendard;
  font-weight: 500;
  background: #ffe6e6;
  padding: 8px 15px;
  border-radius: 8px;
  border: 1px solid #ffcccc;
  white-space: nowrap;
`;

// ---------- CodeProblem 컴포넌트 ----------
const CodeProblem = () => {
  const [language, setLanguage] = useState("프로그래밍 언어");
  const [problemType, setProblemType] = useState("문제 유형");
  const [problemDifficulty, setProblemDifficulty] = useState("");
  const [code, setCode] = useState("");
  const [problemTitle, setProblemTitle] = useState("문제 제목");
  const [problemContent, setProblemContent] = useState("문제 내용");
  const [submissionResult, setSubmissionResult] = useState("");
  const [error, setError] = useState({ show: false, message: "" });

  const handleGetProblem = () => {
    if (language === "프로그래밍 언어" || problemType === "문제 유형" || !problemDifficulty) {
      const missing = [
        language === "프로그래밍 언어" && "언어",
        problemType === "문제 유형" && "문제 유형",
        !problemDifficulty && "난이도",
      ].filter(Boolean);
      setError({ show: true, message: `${missing.join(", ")}을(를) 선택해주세요.` });
      setTimeout(() => setError({ show: false, message: "" }), 4000);
      return;
    }
    setProblemTitle(`${language} ${problemType} 문제`);
    setProblemContent(`${problemDifficulty} 수준의 ${problemType} 문제입니다.`);
    setCode(""); // 초기 코드
    setSubmissionResult("");
  };

  const handleSubmitCode = () => {
    if (!code.trim()) {
      setSubmissionResult("코드를 입력해주세요");
      return;
    }
    if (code.includes("hello") || code.includes("Hello")) {
      setSubmissionResult("정답!");
    } else {
      setSubmissionResult("다시 시도해보세요");
    }
  };

  return (
    <StyledCodeProblem>
      <StyledMainContainer />
      <DropdownLanguage value={language} onChange={(e) => setLanguage(e.target.value)}>
        <option value="프로그래밍 언어" disabled>언어 선택</option>
        <option value="javascript">JavaScript</option>
        <option value="python">Python</option>
        <option value="java">Java</option>
        <option value="c">C</option>
        <option value="cpp">C++</option>
        <option value="typescript">TypeScript</option>
      </DropdownLanguage>

      <DropdownProblemType value={problemType} onChange={(e) => setProblemType(e.target.value)} disabled={language === "프로그래밍 언어"}>
        <option value="문제 유형" disabled>문제 유형 선택</option>
        <option value="출력,변수">출력,변수</option>
        <option value="자료형">자료형</option>
      </DropdownProblemType>

      <DropdownDifficulty value={problemDifficulty} onChange={(e) => setProblemDifficulty(e.target.value)}>
        <option value="" disabled>난이도 선택</option>
        <option value="novice">입문자</option>
        <option value="beginner">초급자</option>
      </DropdownDifficulty>

      <StyledButton style={{ left: '1510px', top: '130px' }} onClick={handleGetProblem}>
        문제 받기
      </StyledButton>

      {error.show && <StyledErrorMessage>⚠️ {error.message}</StyledErrorMessage>}

      <StyledProblemTitle>{problemTitle}</StyledProblemTitle>
      <StyledProblemContent>{problemContent}</StyledProblemContent>

      {problemTitle !== "문제 제목" && (
        <>
          <StyledCodeEditorWrapper>
            <CodeEditor code={code} setCode={setCode} language={language} fontSize={14} />
          </StyledCodeEditorWrapper>
          <StyledButton style={{ left: '1510px', top: '610px' }} onClick={handleSubmitCode}>
            코드 제출
          </StyledButton>
          {submissionResult && <StyledResultDisplay>{submissionResult}</StyledResultDisplay>}
        </>
      )}
    </StyledCodeProblem>
  );
};

export default CodeProblem;