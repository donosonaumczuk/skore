import React from 'react';

const NavBar = () => {
    return (
        <React.Fragment>
            <nav className="navbar fixed-top primary-nav ">
                <button className="navbar-toggler d-md-none" onclick="navbarTogglerClick()" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
                    <span className="fas fa-bars"></span>
                </button>
                <a className="d-none d-sm-block navbar-brand nav-brand-font">sk<i className="fas fa-bullseye"></i>re</a>
                <a className="d-sm-none navbar-brand nav-brand-font"><i className="fas fa-bullseye"></i>sk</a>
                <a className="d-sm-none login-link"><i className="fas fa-user"></i></a>
            
                <form className="d-none d-sm-block form-inline">
                    <a className="mr-1 login-link" >Sign in</a>
                    <span className="white-text mr-1"> or </span>
                    <a className="login-link">create</a>
                </form>   
            </nav>
        </React.Fragment>
    )
}

export default NavBar;