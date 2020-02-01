import React from 'react';
import i18next from 'i18next';
import AuthUserPropType from '../../../../proptypes/AuthUserPropType';
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

const getAdminForm = isAdmin => {
    if (isAdmin) {
        return (
            <form className="d-none d-sm-block form-inline">
                <Link className="login-link" to="/admin">Administrator</Link>
            </form>
        )
    }
    else {
        return <React.Fragment></React.Fragment>
    }
}

const getLoggedForm = currentUser => {
    const  { username } = currentUser;
    const linkReference = `/users/${username}`;
    const adminForm = getAdminForm(currentUser.isAdmin);
    
    return (
        <React.Fragment>
            {adminForm}
            <form className="d-none d-sm-block form-inline">
                <Link className="mr-1 login-link" to={linkReference}>{username}</Link>
                <span className="white-text mr-1"> | </span>
                <Link className="login-link" to="/logout">{i18next.t('navBar.logOut')}</Link>
            </form>
        </React.Fragment>
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