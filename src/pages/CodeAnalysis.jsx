import React from "react";
import styled from "styled-components";

const Container = styled.div`
  /* width: 1920px; */
  height: 1010px;
  position: relative;
  background: #efefef;
  overflow: hidden;
`;

const Panel = styled.div`
  width: 810px;
  height: 860px;
  position: absolute;
  top: 75px;
  background: white;
  box-shadow: 3px 3px 4px rgba(0, 0, 0, 0.3);
  overflow: hidden;
`;

const InputPanel = styled(Panel)`
  left: 137px;
  border-top-left-radius: 60px;
  border-bottom-left-radius: 60px;
`;

const ResultPanel = styled(Panel)`
  left: 973px;
  border-top-right-radius: 60px;
  border-bottom-right-radius: 60px;
`;

const Box = styled.div`
  position: absolute;
  border-radius: 15px;
  background: ${(props) => props.bg || 'white'};
  border: ${(props) => props.border || '3px #B8B8B8 solid'};
  width: ${(props) => props.w || '150px'};
  height: ${(props) => props.h || '60px'};
  left: ${(props) => props.left};
  top: ${(props) => props.top};
`;

const Polygon = styled.div`
  position: absolute;
  width: 17.32px;
  height: 15px;
  background: #d9d9d9;
  left: ${(props) => props.left};
  top: ${(props) => props.top};
`;

const Text = styled.span`
  position: absolute;
  font-family: Inter;
  font-weight: 400;
  font-size: ${(props) => props.size || '20px'};
  color: ${(props) => props.color || 'black'};
  top: ${(props) => props.top};
  left: ${(props) => props.left};
`;

const CodeBox = styled.div`
  position: absolute;
  top: 126px;
  left: 35px;
  width: 740px;
  height: 690px;
  background: #42414b;
  border-radius: 40px;
`;

const CodeText = styled.pre`
  color: white;
  font-size: 32px;
  font-family: Inter;
  font-weight: 400;
  padding: 20px;
`;

const Title = styled.h2`
  position: absolute;
  top: 40px;
  left: 40px;
  font-size: 36px;
  font-family: Inter;
  font-weight: 700;
  color: black;
`;

const ResultBox = styled.div`
  position: absolute;
  width: 250px;
  height: 50px;
  left: 536px;
  background: #d9d9d9;
  border-radius: 15px;
  top: ${(props) => props.top};
`;

export const CodeAnalysis = () => {
  return (
    <Container>
      <InputPanel>
        <Box left="200px" top="35px" />
        <Text top="50px" left="230px">텍스트 입력</Text>
        <Polygon left="317.68px" top="58px" />

        <Box left="35px" top="35px" />
        <Text top="50px" left="55px">JavaScript</Text>
        <Polygon left="158.68px" top="58px" />

        <Box w="100px" left="365px" top="35px" />
        <Text top="50px" left="385px">14px</Text>
        <Polygon left="430px" top="66px" />
        <Polygon left="430px" top="46px" />

        <Box bg="#7DE040" border="none" left="625px" top="35px" />
        <Text top="50px" left="660px">코드분석</Text>

        <CodeBox>
          <CodeText>
{`function hello() {
  console.log("Hello, JavaScript!");
}`}
          </CodeText>
        </CodeBox>
      </InputPanel>

      <ResultPanel>
        <Title>코드 분석 결과</Title>
        <Box w="516px" h="451px" left="0px" top="204px" border="1px solid black" />

        <ResultBox top="275px" />
        <Text top="290px" left="550px">코드 품질:</Text>
        <Text top="290px" left="720px" color="#68B42D" size="15px">84/100</Text>

        <ResultBox top="340px" />
        <Text top="355px" left="550px">보안성:</Text>
        <Text top="355px" left="720px" color="#68B42D" size="15px">79/100</Text>

        <ResultBox top="405px" />
        <Text top="420px" left="550px">성능:</Text>
        <Text top="420px" left="720px" color="#68B42D" size="15px">67/100</Text>

        <ResultBox top="470px" />
        <Text top="485px" left="550px">가독성:</Text>
        <Text top="485px" left="720px" color="#68B42D" size="15px">66/100</Text>

        <ResultBox top="535px" />
        <Text top="550px" left="550px">유지보수성:</Text>
        <Text top="550px" left="720px" color="#68B42D" size="15px">92/100</Text>
      </ResultPanel>
    </Container>
  );
};

export default CodeAnalysis;

