import React, { Component } from 'react';
import LeftPanel from './leftPanel/LeftPanel';

class Home extends Component {
    constructor(props) {
        super(props);
        this.state = {
            matches: null
        };
    }

    render() {
        return (
            <div className="container-fluid">
                <div className="row">
                    <div className="sidepanel col-md-4 col-lg-4 offset-xl-1 
                        col-xl-3 navbar-collapse" id="navbarSupportedContent">
                        <LeftPanel />
                    </div>
                
                    <div className="col-md-8 col-lg-8 col-xl-6">
                        <div className="match-container container-fluid">
                            {/* <MatchCard />*/}
                            {/* <div className="row p-2 mt-2" id="loader">
                                <div className="offset-5 col-2">
                                    <img className="img-fluid" src="<c:url value="/img/loader.gif"/>">
                                </div>
                            </div> */}
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}

export default Home;