import React, { Component } from 'react';
import LeftPanel from './leftPanel/LeftPanel';
import HomeMatches from './HomeMatches';

class Home extends Component {
    mounted = false;
    constructor(props) {
        super(props);
        console.log(props);
        const currentTab = this.props.currentUser ? 1 : 0;
        this.state = {
            currentTab: currentTab,
            filters: {}
        };
    }

    handleTabChange = tabNumber => {
        this.setState({
            currentTab: tabNumber
        });
        //TODO do corresponding post to request matches according to tab
    }

    componentDidMount = () => {
        this.mounted = true;
    }

    updateFilters = filters => {
        if (this.mounted) {
            this.setState({
                filters: filters
            })
        }
    }

    render() {
        let { currentTab } = this.state;
        const { currentUser } = this.props;
        return (
            <div className="container-fluid">
                <div className="row">
                    <div className="sidepanel col-md-4 col-lg-4 offset-xl-1 
                        col-xl-3 navbar-collapse" id="navbarSupportedContent">
                        <LeftPanel currentTab={currentTab} handleTabChange={this.handleTabChange}
                                    currentUser={currentUser} updateFilters={this.updateFilters} />
                    </div>
                    <div className="col-md-8 col-lg-8 col-xl-6">
                            {/* TODO pass down filters when implemented filter search */}
                            <HomeMatches filters={this.state.filters} tab={this.state.currentTab}
                                            currentUser={currentUser} />
                    </div>
                </div>
            </div>
        );
    }

    componentWillUnmount() {
        //TODO stop request
        this.mounted = false;
    }
}

export default Home;