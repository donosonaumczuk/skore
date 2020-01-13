import React from 'react';
import i18next from 'i18next';
import AuthService from './../../services/AuthService';

const getAnonymousForm = () => {
    return (
         <form className="d-none d-sm-block form-inline">
            <a className="mr-1 login-link" href="/login">{i18next.t('navBar.signIn')}</a>
            <span className="white-text mr-1"> or </span>
            <a className="login-link" href="/signUp">{i18next.t('navBar.createAccount')}</a>
        </form>
    );
}

const getLoggedForm = userName => {
    const linkReference = `/users/${userName}`;
    return (
        <form className="d-none d-sm-block form-inline">
            <a className="mr-1 login-link" href={linkReference}>{userName}</a>
            <span className="white-text mr-1"> | </span>
            <a className="login-link" href="/signUp">{i18next.t('navBar.logOut')}</a>
        </form>
    )
}
const navBarForm = () => {
    const currentUser = AuthService.getCurrentUser();
    let form = getAnonymousForm();
    console.log(currentUser);
    if (currentUser) {
        console.log("inside currentUser");
        form = getLoggedForm(currentUser);
    }
    return (<React.Fragment>{form}</React.Fragment>);
}
 
export default navBarForm;