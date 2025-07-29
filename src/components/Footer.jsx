import React from "react";
import styled from "styled-components";
const FooterWrapper = styled.div `
    height : 70px;
    background-color : black;
    display:flex;
    justify-content:center;
`;
const FooterText = styled.p `
    color: white;
`

export const Footer = () => {
    return(
        <FooterWrapper>
            <FooterText>© 2025 MySite. All rights reserved.</FooterText>
        </FooterWrapper>
    );
};
export default Footer;