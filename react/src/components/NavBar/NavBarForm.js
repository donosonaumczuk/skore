import React from 'react';
import i18next from 'i18next';
import Proptypes from 'prop-types';

const getAnonymousForm = () => {
    return (
         <form className="d-none d-sm-block form-inline">
            <a className="mr-1 login-link" href="/login">
                {i18next.t('navBar.signIn')}
            </a>
            <span className="white-text mr-1"> or </span>
            <a className="login-link" href="/signUp">
                {i18next.t('navBar.createAccount')}
            </a>
        </form>
    );
}

const getLoggedForm = userName => {
    const linkReference = `/users/${userName}`;
    return (
        <form className="d-none d-sm-block form-inline">
            <a className="mr-1 login-link" href={linkReference}>{userName}</a>
            <span className="white-text mr-1"> | </span>
            <a className="login-link" href="/logout">{i18next.t('navBar.logOut')}</a>
        </form>
    )
}
const NavBarForm = ({ currentUser }) => {
    let form = getAnonymousForm();
    if (currentUser) {
        form = getLoggedForm(currentUser);
    }
    return (<React.Fragment>{form}</React.Fragment>);
}
 

NavBarForm.propTypes = {
    currentUser: Proptypes.string
}

export default NavBarForm;