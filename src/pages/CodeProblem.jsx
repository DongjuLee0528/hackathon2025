import React from "react";
import styled from "styled-components";

const StyledCodeProblem = styled.div`
  width: 1920px;
  height: 1010px;
  position: relative;
  background: #efefef;
  overflow: hidden;
`;

const Rectangle = styled.div`
  position: absolute;
  width: ${props => props.width}px;
  height: ${props => props.height}px;
  left: ${props => props.left}px;
  top: ${props => props.top}px;
  background: ${props => props.bg || "white"};
  border-radius: ${props => props.borderRadius || 0}px;
  border: ${props => props.border || "none"};
  box-shadow: ${props => props.bg === "white" ? "0px 4px 4px rgba(0, 0, 0, 0.25)" : "none"};
`;

const Polygon = styled.div`
  width: 17.32px;
  height: 15px;
  position: absolute;
  left: ${props => props.left}px;
  top: ${props => props.top}px;
  background: #d9d9d9;
`;

const Line = styled.div`
  width: ${props => props.width}px;
  height: 0;
  position: absolute;
  left: ${props => props.left}px;
  top: ${props => props.top}px;
  transform: rotate(${props => props.rotate}deg);
  transform-origin: top left;
  outline: 1px #bcbcbc solid;
  outline-offset: -0.5px;
`;

const Text = styled.span`
  position: absolute;
  color: ${props => props.color || "black"};
  font-size: ${props => props.size || 20}px;
  font-family: Inter, sans-serif;
  font-weight: 400;
  word-wrap: break-word;
  left: ${props => props.left}px;
  top: ${props => props.top}px;
`;

const CodeBlock = styled.div`
  width: 737px;
  height: 637px;
  left: 1004px;
  top: 258px;
  position: absolute;
  background: #42414b;
  border-radius: 40px;
`;

const CodeText = styled.span`
  position: absolute;
  top: 270px;
  left: 1020px;
  color: white;
  font-size: 32px;
  font-family: Inter, sans-serif;
  font-weight: 400;
  white-space: pre-wrap;
`;

export const CodeProblem = () => {
  return (
    <StyledCodeProblem>
      <Rectangle width={1646} height={860} left={137} top={75} borderRadius={60} />
      <Rectangle width={150} height={60} left={1067} top={110} borderRadius={15} border="3px #B8B8B8 solid" />
      <Text size={20} left={1090} top={125}>JavaScript</Text>
      <Polygon left={1190.68} top={133} />

      <Rectangle width={150} height={60} left={1232} top={110} borderRadius={15} border="3px #B8B8B8 solid" />
      <Text size={20} left={1255} top={125}>문제 유형</Text>
      <Polygon left={1355.68} top={133} />

      <Rectangle width={150} height={60} left={1397} top={110} borderRadius={15} border="3px #B8B8B8 solid" />
      <Text size={20} left={1420} top={125}>문제 난이도</Text>
      <Polygon left={1520.68} top={133} />

      <Rectangle width={150} height={60} left={1562} top={110} borderRadius={15} bg="#7DE040" />
      <Text size={20} left={1585} top={125}>문제 받기</Text>

      <Text size={60} left={200} top={120}>문제 제목</Text>
      <Text size={20} left={200} top={200}>문제 내용</Text>

      <CodeBlock />
      <CodeText>{`function hello() {\n  console.log("Hello, JavaScript!");\n}`}</CodeText>

      <Line width={788} left={960} top={118} rotate={90} />
      <Line width={780} left={960} top={218} rotate={180} />
      <Line width={780} left={1740} top={218} rotate={180} />
    </StyledCodeProblem>
  );
};

export default CodeProblem;
