import React from 'react';
import i18next from 'i18next';
import Proptypes from 'prop-types';
import { Link } from 'react-router-dom';

const getAnonymousForm = () => {
    return (
         <form className="d-none d-sm-block form-inline">
            <Link className="mr-1 login-link" to="/login">
                {i18next.t('navBar.signIn')}
            </Link>
            <span className="white-text mr-1"> or </span>
            <Link className="login-link" to="/signUp">
                {i18next.t('navBar.createAccount')}
            </Link>
        </form>
    );
}

const getLoggedForm = userName => {
    const linkReference = `/users/${userName}`;
    return (
        <form className="d-none d-sm-block form-inline">
            <Link className="mr-1 login-link" to={linkReference}>{userName}</Link>
            <span className="white-text mr-1"> | </span>
            <Link className="login-link" to="/logout">{i18next.t('navBar.logOut')}</Link>
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