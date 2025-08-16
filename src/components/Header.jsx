import React, { useEffect,useState  } from "react";
import styled from "styled-components";
import { Link } from "react-router-dom";
import axios from "axios";
// 🔷 전체 헤더를 감싸는 Wrapper
const HeaderWrapper = styled.div`
  height: 70px;
  position: relative;
  background: white;
  overflow: hidden;
`;

// 🔷 상단 테두리 (겉 라인 역할)
const HeaderBorder = styled.div`
  height: 72px;
  position: absolute;
  top: -1px;
  left: -1px;
  background: white;
  // border: 1px solid black; // 테두리 추가 가능
  z-index: 1; // 로고나 메뉴보다 아래에 위치
`;

// 🔷 네비게이션 메뉴를 가운데 정렬해주는 컨테이너
const NavContainer = styled.div`
  position: absolute;
  top: 10px;
  left: 50%;
  transform: translateX(-50%);
  display: flex;
  gap: 150px;
  z-index: 2; // 로고보다 뒤에 있으면 안 보일 수 있음
`;

// 🔷 네비게이션 메뉴 항목
const NavItem = styled(Link)`
  color: black;
  font-size: 29px;
  font-family: Inter;
  font-weight: 700;
  cursor: pointer;
  position: relative;
  transition: color 0.2s ease;
  text-decoration: none; /* 기본 밑줄 제거 */

  &:hover {
    color: #2c2c2c;
  }

  &:hover::after {
    /* 밑줄 애니메이션 */
    content: "";
    position: absolute;
    bottom: -5px;
    left: 0;
    width: 100%;
    height: 3px;
    background-color: black;
  }
`;

// 🔷 GitHub 로그인 버튼 (둥근 검은 배경 박스)
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
    transform: scale(1.03); // 살짝 확대
  }
`;

// 🔷 GitHub 버튼 안의 텍스트
const GitHubButtonText = styled.div`
  color: white;
  font-size: 16px;
  font-family: Inter;
  font-weight: 400;
  position: absolute;
  left: 1660px;
  top: 24px;
  z-index: 3;
  pointer-events: none; // 클릭 이벤트 방지
`;

// 🔷 GitHub 아이콘을 감싸는 영역
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

// 🔷 로고 이미지 스타일
const LogoImage = styled.img`
  width: 224px;
  height: 77px;
  position: absolute;
  left: 137px;
  top: -3px;
  z-index: 3;
  transition: transform 0.2s ease;

  &:hover {
    transform: scale(1.05); // 살짝 확대 효과
  }
`;

// 🔷 GitHub 아이콘 (SVG로 구현)
const GitHubIcon = () => (
  <svg
    xmlns="http://www.w3.org/2000/svg"
    width="25"
    height="25"
    viewBox="0 0 24 24"
    fill="white"
  >
    <path d="M12 0.5C5.649 0.5 0.5 5.649 0.5 12C0.5 17.303 4.157 21.8 9.096 23.238C9.656 23.338 9.864 23.015 9.864 22.736C9.864 22.486 9.854 21.736 9.849 20.736C6.625 21.386 5.967 19.119 5.967 19.119C5.464 17.736 4.737 17.386 4.737 17.386C3.703 16.736 4.814 16.75 4.814 16.75C5.948 16.836 6.567 17.942 6.567 17.942C7.567 19.619 9.122 19.142 9.737 18.886C9.837 18.169 10.122 17.669 10.437 17.386C7.92 17.103 5.291 16.086 5.291 11.875C5.291 10.625 5.737 9.636 6.475 8.886C6.35 8.603 5.954 7.469 6.596 5.936C6.596 5.936 7.525 5.636 9.85 7.25C10.728 7.003 11.67 6.878 12.61 6.872C13.55 6.878 14.492 7.003 15.37 7.25C17.695 5.636 18.624 5.936 18.624 5.936C19.266 7.469 18.87 8.603 18.745 8.886C19.484 9.636 19.929 10.625 19.929 11.875C19.929 16.1 17.293 17.1 14.766 17.375C15.183 17.736 15.567 18.486 15.567 19.636C15.567 21.286 15.554 22.386 15.554 22.736C15.554 23.015 15.761 23.343 16.326 23.238C21.263 21.799 24.919 17.302 24.919 12C24.919 5.649 19.77 0.5 13.419 0.5H12Z" />
  </svg>
);

  


// 🔷 Header 컴포넌트 정의
export const Header = () => {
  const [repos, setRepos] = useState([]);
  useEffect(() => {
    // 깃허브 api 호출
      axios.get("http://thecoder.djloghub.com/login/oauth2/code/github")
    .then(res=> {
      setRepos(res.data);
    })
    .catch(err => console.error("에러:",err));
  }, []);
  
  const handleGithubLogin = () => {
    // 깃허브 로그인 창 주소
    window.location.href = "https://thecoder.djloghub.com/oauth2/authorization/github";
  };
  return (
    <HeaderWrapper>
      {/* 좌측 상단 로고 , homepage 이동*/}
      <Link to="/">
        <LogoImage src="/Logo.png" alt="로고" />
      </Link>


      {/* 헤더 배경 박스 (겉 테두리) */}
      <HeaderBorder />

      {/* 중앙 네비게이션 메뉴 다른 페이지 이동*/}
      <NavContainer>
        <NavItem as={Link} to="/CodeAnalysis">코드분석</NavItem>
        <NavItem as={Link} to="/CodeRefactoring">코드개선</NavItem>
        <NavItem as={Link} to="/CodeProblem">문제풀기</NavItem>
      </NavContainer>

      {/* 우측 GitHub 로그인 버튼 */}
      <GitHubButton onclick={handleGithubLogin}/>
      <GitHubButtonText>Git Hub 로그인</GitHubButtonText>
      <GitHubIconWrapper>
        <GitHubIcon />
      </GitHubIconWrapper>
    </HeaderWrapper>
  );
};

export default Header;
