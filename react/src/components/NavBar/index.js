import React from 'react';
import AuthUserPropType from '../../proptypes/AuthUserPropType';
import NavBarForm from './components/NavBarForm';
import { Link } from 'react-router-dom';

const NavBar = (props) => {
    return (
        <React.Fragment>
            <nav className="navbar fixed-top primary-nav ">
                <button className="navbar-toggler d-md-none" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
                    <span className="fas fa-bars"></span>
                </button>
                <Link className="d-none d-sm-block navbar-brand nav-brand-font" to="/">sk<i className="fas fa-bullseye"></i>re</Link>
                <Link className="d-sm-none navbar-brand nav-brand-font" to="/"><i className="fas fa-bullseye"></i>sk</Link>
                <Link className="d-sm-none login-link" to="/"><i className="fas fa-user"></i></Link>
                <NavBarForm currentUser={props.currentUser} /> 
            </nav>
        </React.Fragment>
    )
}

NavBar.propTypes = {
    currentUser: AuthUserPropType.isRequired
}

export default NavBar;