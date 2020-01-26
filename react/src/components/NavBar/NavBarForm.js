import React from 'react';
import i18next from 'i18next';
import AuthUserPropType from './../../proptypes/AuthUserPropType';
import { Link } from 'react-router-dom';

const getAnonymousForm = () => {
    return (
         <form className="d-none d-sm-block form-inline">
            <Link className="mr-1 login-link" to="/login">
                {i18next.t('navBar.signIn')}
            </Link>
            <span className="white-text mr-1">{i18next.t('navBar.or')}</span>
            <Link className="login-link" to="/signUp">
                {i18next.t('navBar.createAccount')}
            </Link>
        </form>
    );
}

const getLoggedForm = currentUser => {
    const  { username } = currentUser;
    const linkReference = `/users/${username}`;
    return (
        <form className="d-none d-sm-block form-inline">
            <Link className="mr-1 login-link" to={linkReference}>{username}</Link>
            <span className="white-text mr-1"> | </span>
            <Link className="login-link" to="/logout">{i18next.t('navBar.logOut')}</Link>
        </form>
    );
}
const NavBarForm = ({ currentUser }) => {
    let form = getAnonymousForm();
    if (currentUser.username) {
        form = getLoggedForm(currentUser);
    }
    return (<React.Fragment>{form}</React.Fragment>);
}
 

NavBarForm.propTypes = {
    currentUser: AuthUserPropType.isRequired
}

export default NavBarForm;