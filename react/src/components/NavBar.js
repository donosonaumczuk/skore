import React from 'react';
import { Link } from 'react-router-dom';

//TODO maybe use link instead of href an check that is valid to use href directly
// i dont remember why was it that in jsp we used to put <c:url> and here we use it directly
const NavBar = () => {
    return (
        <React.Fragment>
            <nav className="navbar fixed-top primary-nav ">
                <button className="navbar-toggler d-md-none" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
                    <span className="fas fa-bars"></span>
                </button>
                <a className="d-none d-sm-block navbar-brand nav-brand-font" href="/">sk<i className="fas fa-bullseye"></i>re</a>
                <a className="d-sm-none navbar-brand nav-brand-font" href="/createUser"><i className="fas fa-bullseye"></i>sk</a>
                <a className="d-sm-none login-link" href="/createUser"><i className="fas fa-user"></i></a>
            
                <form className="d-none d-sm-block form-inline">
                    <a className="mr-1 login-link" href="/createUser">Sign in</a>
                    <span className="white-text mr-1"> or </span>
                    <a className="login-link" href="/createUser">create</a>
                </form>   
            </nav>
        </React.Fragment>
    )
}

export default NavBar;