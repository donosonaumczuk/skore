import React, { Component } from 'react';
import MatchService from './../../services/MatchService';
import LeftPanel from './leftPanel/LeftPanel';
import Loader from './../Loader';
import HomeMatches from './HomeMatches';
class Home extends Component {
    constructor(props) {
        super(props);
        this.state = {
            matches: null
        };
    }

    getMatches = matches => {
        if (matches == null) 
        {
            return <Loader />
        }
        else {
            return <HomeMatches matches={matches} />
        }
    }

    async componentDidMount() {
        console.log("executing");
        let matches = await MatchService.getMatches();
        this.setState({
            matches: matches
        });
    }

    render() {
        let matches = this.getMatches(this.state.matches);
        
        return (
            <div className="container-fluid">
                <div className="row">
                    <div className="sidepanel col-md-4 col-lg-4 offset-xl-1 
                        col-xl-3 navbar-collapse" id="navbarSupportedContent">
                        <LeftPanel />
                    </div>
                
                    <div className="col-md-8 col-lg-8 col-xl-6">
                        
                            {/* <MatchCard />*/}
                            {matches}
                    
                    </div>
                </div>
            </div>
        );
    }

    componentWillUnmount() {
        //TODO stop request
    }
}

export default Home;