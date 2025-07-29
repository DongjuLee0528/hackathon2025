// React Router에서 필요한 컴포넌트들을 가져옴
import { Routes, Route, Link, BrowserRouter } from 'react-router-dom';

// 각각의 페이지 및 컴포넌트 import
import Home from './pages/Home';
import Header from './components/Header';
import Footer from './components/Footer';
import CodeAnalysis from './pages/CodeAnalysis';
import CodeRefactoring from './pages/CodeRefactoring';
import CodeProblem from './pages/CodeProblem';

// 전역 CSS 파일 import
import './App.css';

// App 컴포넌트 정의
function App() {
  return (
    <>
      {/* 공통 상단 헤더 영역 */}
      <Header />

      {/* 페이지별 라우팅 처리 영역 */}
      <Routes>
        {/* "/" 경로 → Home 컴포넌트 */}
        <Route path="/" element={<Home />} />

        {/* "/CodeAnalysis" 경로 → 코드 분석 페이지 */}
        <Route path="/CodeAnalysis" element={<CodeAnalysis />} />

        {/* "/CodeRefactoring" 경로 → 코드 리팩토링 페이지 */}
        <Route path="/CodeRefactoring" element={<CodeRefactoring />} />

        {/* "/CodeProblem" 경로 → 문제 풀이 페이지 */}
        <Route path="/CodeProblem" element={<CodeProblem />} />
      </Routes>

      {/* 공통 하단 푸터 영역 */}
      <Footer />
    </>
  );
}

// App 컴포넌트 export → index.js에서 사용됨
export default App;