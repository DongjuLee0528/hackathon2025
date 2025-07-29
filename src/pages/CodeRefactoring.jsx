import React from "react";
import styled from "styled-components";

const Container = styled.div`
  height: 1010px;
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
  overflow: hidden;
  border-top-left-radius: 60px;
  border-bottom-left-radius: 60px;
`;

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
`;

const RectangularBox = styled.div`
  width: 150px;
  height: 60px;
  position: absolute;
  background: white;
  border-radius: 15px;
  border: 3px solid #b8b8b8;
`;

const Rect1 = styled(RectangularBox)`
  left: 35px;
  top: 35px;
`;

const Rect2 = styled(RectangularBox)`
  left: 200px;
  top: 35px;
`;

const Rect3 = styled.div`
  width: 100px;
  height: 60px;
  left: 365px;
  top: 35px;
  position: absolute;
  background: white;
  border-radius: 15px;
  border: 3px solid #b8b8b8;
`;

const Rect4 = styled.div`
  width: 150px;
  height: 60px;
  left: 625px;
  top: 35px;
  position: absolute;
  background: #7de040;
  border-radius: 15px;
`;

const Polygon = styled.div`
  width: 17.32px;
  height: 15px;
  position: absolute;
  background: #d9d9d9;
`;

const Polygon1 = styled(Polygon)`
  left: 158.68px;
  top: 58px;
`;

const Polygon2 = styled(Polygon)`
  left: 317.68px;
  top: 58px;
`;

const Polygon3 = styled(Polygon)`
  left: 430px;
  top: 66px;
`;

const Polygon4 = styled(Polygon)`
  left: 430px;
  top: 46px;
`;

const Text = styled.span`
  font-family: Inter, sans-serif;
  font-weight: 400;
  word-wrap: break-word;
  color: black;
  position: absolute;
`;

const Text1 = styled(Text)`
  font-size: 20px;
  left: 50px;
  top: 50px;
`;

const Text2 = styled(Text)`
  font-size: 20px;
  left: 215px;
  top: 50px;
`;

const Text3 = styled(Text)`
  font-size: 20px;
  left: 375px;
  top: 50px;
`;

const Text4 = styled(Text)`
  font-size: 32px;
  left: 635px;
  top: 50px;
`;

const CodeBlock = styled.pre`
  color: white;
  font-family: Inter, sans-serif;
  font-size: 32px;
  font-weight: 400;
  white-space: pre-wrap;
  position: absolute;
  top: 150px;
  left: 50px;
  width: 700px;
  height: 600px;
`;

const Title = styled.h2`
  color: black;
  font-family: Inter, sans-serif;
  font-weight: 700;
  font-size: 36px;
  position: absolute;
  top: 10px;
  left: 50px;
`;

const Subtitle = styled.p`
  color: #737373;
  font-family: Inter, sans-serif;
  font-weight: 400;
  font-size: 24px;
  position: absolute;
  top: 50px;
  left: 50px;
`;

export const CodeRefactoring = () => {
  return (
    <Container>
      <Frame>
        <Rect1 />
        <Text1>텍스트 입력</Text1>
        <Polygon2 />
        <Rect2 />
        <Text2>JavaScript</Text2>
        <Polygon1 />
        <Rect3 />
        <Text3>14px</Text3>
        <Polygon3 />
        <Polygon4 />
        <Rect4 />
        <Text4>코드개선</Text4>
        <CodeBlock>
{`function hello() {
  console.log("Hello, JavaScript!");
}`}
        </CodeBlock>
      </Frame>

      <CodeAnalysisContainer>
        <Title>코드 개선 결과</Title>
        <Subtitle>
          아직 개선 결과가 없습니다.
          <br />
          코드를 작성하거나 파일을 업로드한 후 '코드 개선' 버튼을 클릭하세요.
        </Subtitle>
      </CodeAnalysisContainer>
    </Container>
  );
};

export default CodeRefactoring;
