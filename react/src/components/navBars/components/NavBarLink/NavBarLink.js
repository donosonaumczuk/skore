import React from 'react';
import Proptypes from 'prop-types';

const getItalicTag = (hasItalics, italicText, italicStyle) => {
    let italicTag = <React.Fragment></React.Fragment>;
    if (hasItalics) {
        italicTag = <i className={italicStyle}>{italicText}</i>
    }
    return italicTag;
}

const NavBarLink = (linkStyle, linkReference, textBeforeItalics, hasItalics, italicText, italicStyle, textAfterItalics ) => {
    
    return (
         <a className={linkStyle} href={linkReference}>
            {textBeforeItalics}
            {getItalicTag(hasItalics, italicText, italicStyle)}
            {textAfterItalics}
        </a>          
    );
}

NavBarLink.propTypes = {
    linkStyle: Proptypes.string.isRequired,
    linkReference: Proptypes.string.isRequired,
    textBeforeItalics: Proptypes.string.isRequired,
    hasItalics: Proptypes.bool.isRequired,
    italicText: Proptypes.string.isRequired,
    italicStyle: Proptypes.string.isRequired,
    textAfterItalics: Proptypes.string.isRequired
}

export default NavBarLink;