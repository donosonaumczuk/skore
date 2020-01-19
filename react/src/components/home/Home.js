import React, { Component } from 'react';
import MatchService from './../../services/MatchService';
import LeftPanel from './leftPanel/LeftPanel';
import Loader from './../Loader';
import HomeMatches from './HomeMatches';

class Home extends Component {
    constructor(props) {
        super(props);
        this.state = {
            matches: null,
            currentTab: 1
        };
    }

    getMatches = matches => {
        if (matches == null) {
            return <Loader />
        }
        else {
            return <HomeMatches matches={matches} />
        }
    }

    handleTabChange = tabNumber => {
        this.setState({
            currentTab: tabNumber
        });
        //TODO do corresponding post to request matches according to tab
    }

    async componentDidMount() {
        let matches = await MatchService.getMatches();
        this.setState({
            matches: matches
        });
    }

    render() {
        let matches = this.getMatches(this.state.matches);
        let { currentTab } = this.state;
        return (
            <div className="container-fluid">
                <div className="row">
                    <div className="sidepanel col-md-4 col-lg-4 offset-xl-1 
                        col-xl-3 navbar-collapse" id="navbarSupportedContent">
                        <LeftPanel currentTab={currentTab} handleTabChange={this.handleTabChange} />
                    </div>
                
                    <div className="col-md-8 col-lg-8 col-xl-6">
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