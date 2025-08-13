// CodeAnalysis.jsx
import React from "react";
import styled from "styled-components";

const Container = styled.div`
  min-height: 100vh;
  background: #efefef;
  display: flex;
  justify-content: center;
  align-items: flex-start;
  padding: 50px 20px;
  gap: 60px;
  flex-wrap: wrap;

  @media (max-width: 900px) {
    gap: 30px;
    padding: 20px 10px;
  }
`;

const Panel = styled.div`
  background: white;
  box-shadow: 3px 3px 8px rgba(0, 0, 0, 0.2);
  border-radius: 40px;
  display: flex;
  flex-direction: column;
`;

const InputPanel = styled(Panel)`
  width: 810px;
  min-height: 860px;
  padding: 40px 35px;

  @media (max-width: 900px) {
    width: 100%;
    min-height: auto;
  }
`;

const ResultPanel = styled(Panel)`
  width: 810px;
  min-height: 860px;
  padding: 40px 50px;

  @media (max-width: 900px) {
    width: 100%;
    min-height: auto;
  }
`;

const DropdownRow = styled.div`
  display: flex;
  gap: 20px;
  margin-bottom: 40px;
  align-items: center;
  flex-wrap: wrap;
`;

const Box = styled.div`
  flex-shrink: 0;
  width: ${(props) => props.w || "150px"};
  height: ${(props) => props.h || "60px"};
  background: ${(props) => props.bg || "white"};
  border-radius: 15px;
  border: ${(props) => props.border || "3px solid #B8B8B8"};
  display: flex;
  justify-content: center;
  align-items: center;
  font-family: Inter, sans-serif;
  font-weight: 400;
  font-size: 18px;
  color: ${(props) => props.color || "black"};
  position: relative;
  cursor: pointer;
  user-select: none;
  transition: all 0.2s ease;

  &:hover {
    border-color: #4dabf7;
    color: #4dabf7;
  }
`;

const Arrow = styled.div`
  position: absolute;
  right: 10px;
  top: 50%;
  width: 0; 
  height: 0; 
  border-left: 7px solid transparent;
  border-right: 7px solid transparent;
  border-top: 8px solid #d9d9d9;
  transform: translateY(-50%);
`;

const CodeBox = styled.pre`
  background: #2d2d2d;
  border-radius: 20px;
  color: #f8f9fa;
  font-size: 16px;
  font-family: 'Consolas', 'Monaco', 'Courier New', monospace;
  padding: 20px 25px;
  height: 690px;
  overflow-y: auto;
  white-space: pre-wrap;
  word-break: break-word;
  line-height: 1.6;

  /* 스크롤바 커스터마이징 */
  &::-webkit-scrollbar {
    width: 8px;
  }
  &::-webkit-scrollbar-thumb {
    background: #868e96;
    border-radius: 4px;
  }
  &::-webkit-scrollbar-thumb:hover {
    background: #495057;
  }

  @media (max-width: 900px) {
    height: 400px;
    font-size: 14px;
  }
`;

const Title = styled.h2`
  font-size: 28px;
  font-family: Inter, sans-serif;
  font-weight: 700;
  color: black;
  margin-bottom: 30px;
`;

const ResultList = styled.div`
  border: 1px solid #dee2e6;
  border-radius: 15px;
  padding: 25px 30px;
  height: 450px;
  display: flex;
  flex-direction: column;
  justify-content: space-around;
  background-color: #f8f9fa;

  @media (max-width: 900px) {
    height: auto;
    padding: 20px;
  }
`;

const ResultItem = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
`;

const ResultLabel = styled.span`
  font-family: Inter, sans-serif;
  font-weight: 500;
  font-size: 18px;
  color: black;
`;

const ResultScore = styled.span`
  font-family: Inter, sans-serif;
  font-weight: 600;
  font-size: 16px;
  color: #37b24d;
`;

const AnalyzeButton = styled(Box)`
  background: #7de040;
  border: none;
  color: black;
  font-weight: 700;
  cursor: pointer;
  width: 120px;
  height: 60px;

  &:hover {
    background: #6bc935;
    transform: translateY(-1px);
  }
  &:active {
    transform: translateY(0);
  }
`;

const CodeAnalysis = () => {
  const results = [
    { label: "코드 품질", score: "84/100" },
    { label: "보안성", score: "79/100" },
    { label: "성능", score: "67/100" },
    { label: "가독성", score: "66/100" },
    { label: "유지보수성", score: "92/100" },
  ];

  return (
    <Container>
      <InputPanel>
        <DropdownRow>
          <Box>JavaScript <Arrow /></Box>
          <Box w="100px">14px <Arrow /></Box>
          <AnalyzeButton>코드분석</AnalyzeButton>
        </DropdownRow>

        <CodeBox>
{`function hello() {
  console.log("Hello, JavaScript!");
}`}
        </CodeBox>
      </InputPanel>

      <ResultPanel>
        <Title>코드 분석 결과</Title>
        <ResultList>
          {results.map(({ label, score }, idx) => (
            <ResultItem key={idx}>
              <ResultLabel>{label}</ResultLabel>
              <ResultScore>{score}</ResultScore>
            </ResultItem>
          ))}
        </ResultList>
      </ResultPanel>
    </Container>
  );
};

export default CodeAnalysis;
