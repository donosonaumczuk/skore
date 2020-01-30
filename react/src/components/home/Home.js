import React, { Component } from 'react';
import queryString from 'query-string';
import LeftPanel from './leftPanel/LeftPanel';
import HomeMatches from './HomeMatches';
import MatchService from '../../services/MatchService';
import { buildUrlFromParamQueries } from '../../services/Util';
import Utils from '../utils/Utils';
import Loader from '../Loader';
import ErrorPage from './../ErrorPage';

const INITIAL_OFFSET = 0;
const QUERY_QUANTITY = 5;

class Home extends Component {
    mounted = false;
    constructor(props) {
        super(props);
        const currentTab = this.props.currentUser ? 1 : 0;
        const queryParams = queryString.parse(this.props.location.search);
        this.state = {
            currentTab: currentTab,
            filters: queryParams,
            matches: [],
            offset: INITIAL_OFFSET,
            total: QUERY_QUANTITY,
            hasMore: true,
        };
    }

    handleTabChange = tabNumber => {
        console.log("handle tab", tabNumber);
        if (tabNumber !== this.state.currentTab && this.mounted) {
            this.setState({
                matches: [],
                offset: INITIAL_OFFSET,
                hasMore: true,
                currentTab: tabNumber
            }, () => { this.getMatches();}); 
        }
    }

    componentDidMount = () => {
        this.mounted = true;
        this.getMatches();
    }

    updateFilters = filters => {
        const newUrl = buildUrlFromParamQueries(filters);
        if (this.mounted) {
            this.setState({
                matches: [],
                offset: INITIAL_OFFSET,
                hasMore: true,
                filters: filters
            }, () => { this.getMatches();}); 
            this.props.history.push(`/${newUrl}`);
        }  
    }

    updateMatches = response => {
        if (response.status) {
            this.setState({ status: response.status });
        }
        else {
            const matches = response.matches;
            const hasMore = Utils.hasMorePages(response.links);
            this.setState({
                matches: [...this.state.matches, ...matches],
                offset: this.state.offset + matches.length,
                hasMore: hasMore
            });  
        }
    }

    getMatches = async () => {
        let response;
        const { currentUser } = this.props;
        const { offset, total, currentTab, filters } = this.state;
        if (currentTab === 0) {
            response = await MatchService.getMatches(offset, total);
        }
        else if (currentTab === 1) {
            response = await MatchService.getMatchesToJoin(currentUser, offset, total, filters);
        }
        else if (currentTab === 2) {
            response = await MatchService.getMatchesJoinedBy(currentUser, offset, total, filters);
        }
        else if (currentTab === 3) {
            response = await MatchService.getMatchesCreatedBy(currentUser, offset, total, filters);
        }
        if (this.mounted) {
            this.updateMatches(response);
        }
    }

    render() {
        let { currentTab, matches, hasMore } = this.state;
        const { currentUser } = this.props;
        let currentMatches;
        if (this.state.status) {
            currentMatches = <ErrorPage status={this.state.status} />;
        }
        else if (matches.length === 0 && hasMore) {
            currentMatches = <Loader />;
        }
        else {
            currentMatches = <HomeMatches matches={matches} hasMore={hasMore} getMatches={this.getMatches} />;
        }
        return (
            <div className="container-fluid">
                <div className="row">
                    <div className="sidepanel col-md-4 col-lg-4 offset-xl-1 
                        col-xl-3 navbar-collapse" id="navbarSupportedContent">
                        <LeftPanel currentTab={currentTab} handleTabChange={this.handleTabChange}
                                    currentUser={currentUser} filters={this.state.filters}
                                    updateFilters={this.updateFilters} />
                    </div>
                    <div className="col-md-8 col-lg-8 col-xl-6">
                        {currentMatches}
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