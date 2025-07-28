import React from "react";
import styled from "styled-components";

// 전체 헤더 감싸는 영역
const HeaderWrapper = styled.div`
  height: 70px;
  position: relative;
  background: white;
  overflow: hidden;
`;

// 상단 테두리
const HeaderBorder = styled.div`
  height: 72px;
  position: absolute;
  top: -1px;
  left: -1px;
  background: white;
  // border: 1px solid black;
  z-index: 1;
`;

// 네비게이션 메뉴 컨테이너 (가운데 정렬)
const NavContainer = styled.div`
  position: absolute;
  top: 10px;
  left: 50%;
  transform: translateX(-50%);
  display: flex;
  gap: 150px;
  z-index: 2;
`;

// 네비게이션 항목 (hover 시 강조 효과)
const NavItem = styled.div`
  color: black;
  font-size: 29px;
  font-family: Inter;
  font-weight: 700;
  cursor: pointer;
  position: relative;
  transition: color 0.2s ease;

  &:hover {
    color: #2c2c2c;
  }

  &:hover::after {
    content: "";
    position: absolute;
    bottom: -5px;
    left: 0;
    width: 100%;
    height: 3px;
    background-color: black;
  }
`;

// GitHub 로그인 버튼 (hover 시 확대 및 색 변화)
const GitHubButton = styled.div`
  width: 170px;
  height: 50px;
  position: absolute;
  left: 1613px;
  top: 10px;
  background: black;
  border-radius: 50px;
  z-index: 2;
  transition: background-color 0.2s ease, transform 0.2s ease;

  &:hover {
    background-color: #333333;
    transform: scale(1.03);
  }
`;

// GitHub 버튼 텍스트 (hover 대상에서 제외)
const GitHubButtonText = styled.div`
  color: white;
  font-size: 16px;
  font-family: Inter;
  font-weight: 400;
  position: absolute;
  left: 1660px;
  top: 24px;
  z-index: 3;
  pointer-events: none;
`;

// GitHub 아이콘 컨테이너
const GitHubIconWrapper = styled.div`
  width: 25px;
  height: 25px;
  position: absolute;
  left: 1627px;
  top: 25px;
  overflow: hidden;
  z-index: 3;
  pointer-events: none;
`;

// 로고 이미지 (hover 시 확대 효과)
const LogoImage = styled.img`
  width: 224px;
  height: 77px;
  position: absolute;
  left: 137px;
  top: -3px;
  z-index: 3;
  transition: transform 0.2s ease;

  &:hover {
    transform: scale(1.05);
  }
`;

// GitHub 아이콘 (SVG)
const GitHubIcon = () => (
  <svg
    xmlns="http://www.w3.org/2000/svg"
    width="25"
    height="25"
    viewBox="0 0 24 24"
    fill="white"
  >
    <path d="M12 0C5.37 0 0 5.37 0 12c0 5.3 3.438 9.8 8.205 11.387.6.113.82-.262.82-.583 0-.288-.01-1.05-.015-2.06-3.338.726-4.042-1.61-4.042-1.61-.546-1.387-1.332-1.755-1.332-1.755-1.09-.745.083-.73.083-.73 1.205.085 1.84 1.237 1.84 1.237 1.07 1.835 2.805 1.305 3.49.997.108-.776.418-1.305.76-1.605-2.665-.3-5.466-1.332-5.466-5.931 0-1.31.467-2.381 1.235-3.221-.124-.303-.535-1.523.116-3.176 0 0 1.008-.322 3.3 1.23a11.52 11.52 0 013.003-.403c1.02.004 2.045.137 3.003.403 2.29-1.552 3.295-1.23 3.295-1.23.653 1.653.242 2.873.118 3.176.77.84 1.233 1.911 1.233 3.221 0 4.61-2.805 5.628-5.475 5.922.43.37.823 1.102.823 2.222 0 1.604-.015 2.896-.015 3.29 0 .324.216.7.825.58C20.565 21.796 24 17.297 24 12 24 5.37 18.63 0 12 0z" />
  </svg>
);

// Header 컴포넌트
export const Header = () => {
  return (
    <HeaderWrapper>
      {/* 로고 이미지 */}
      <LogoImage src="/Logo.png" />

      {/* 상단 테두리 */}
      <HeaderBorder />

      {/* 네비게이션 */}
      <NavContainer>
        <NavItem>코드분석</NavItem>
        <NavItem>문제풀기</NavItem>
        <NavItem>코드개선</NavItem>
      </NavContainer>

      {/* GitHub 버튼 */}
      <GitHubButton />
      <GitHubButtonText>Git Hub 로그인</GitHubButtonText>
      <GitHubIconWrapper>
        <GitHubIcon />
      </GitHubIconWrapper>
    </HeaderWrapper>
  );
};

export default Header;
