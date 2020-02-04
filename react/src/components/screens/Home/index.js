import React, { Component } from 'react';
import queryString from 'query-string';
import PropTypes from 'prop-types';
import HomeMatches from './components/HomeMatches';
import MatchService from '../../../services/MatchService';
import Utils from '../../utils/Utils';
import Loader from '../../Loader';
import ErrorPage from '../ErrorPage';
import Home from './layout';

const INITIAL_OFFSET = 0;
const QUERY_QUANTITY = 5;
const MIN_TAB = 0;
const MAX_TAB = 3;

const getCurrentTab = (currentUser, tab) => {
    let currentTab = 0;
    if (currentUser) {
        if (tab && tab > MIN_TAB && tab <= MAX_TAB) {
            currentTab = parseInt(tab);
        }
        else {
            currentTab = 1;
        }
    }
    return currentTab;
}

class HomeContainer extends Component {
    mounted = false;
    constructor(props) {
        super(props);
        const queryParams = queryString.parse(this.props.location.search);
        const currentTab = getCurrentTab(this.props.currentUser, queryParams.tab);
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
        if (tabNumber !== this.state.currentTab && this.mounted) {
            const newUrl = Utils.buildUrlFromParamQueriesAndTab(this.state.filters, tabNumber);
            this.setState({
                matches: [],
                offset: INITIAL_OFFSET,
                hasMore: true,
                currentTab: tabNumber
            }, () => { this.getMatches(); }); 
            this.props.history.push(`/${newUrl}`);
        }
    }

    handleMatchClick = matchKey => {
        this.props.history.push(`/match/${matchKey}`);
    }

    componentDidMount = () => {
        this.mounted = true;
        this.getMatches();
    }

    updateFilters = filters => {
        const newUrl = Utils.buildUrlFromParamQueriesAndTab(filters, this.state.currentTab);
        if (this.mounted) {
            this.setState({
                matches: [],
                offset: INITIAL_OFFSET,
                hasMore: true,
                filters: filters
            }, () => { this.getMatches(); }); 
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
        const newFilters = Utils.removeUnknownHomeFilters(filters);
        if (currentTab === 0) {
            response = await MatchService.getMatches(offset, total, newFilters);
        }
        else if (currentTab === 1) {
            response = await MatchService.getMatchesToJoin(currentUser, offset, total, newFilters);
        }
        else if (currentTab === 2) {
            response = await MatchService.getMatchesJoinedBy(currentUser, offset, total, newFilters);
        }
        else if (currentTab === 3) {
            response = await MatchService.getMatchesCreatedBy(currentUser, offset, total, newFilters);
        }
        if (this.mounted) {
            this.updateMatches(response);
        }
    }

    joinMatch = (e, match) => {
        e.stopPropagation();
        console.log("join match: ", match.title);
        // TODO implement when we have endpoint
    }
    
    cancelMatch = (e, match) => {
        e.stopPropagation();
        console.log("cancel match: ", match.title);
        // TODO implement when we have endpoint
    }
    
    deleteMatch = async (e, match) => {
        e.stopPropagation();
        this.setState({ loading: true });
        const response = await MatchService.deleteMatch(match.key);
        if (response.status && this.mounted) {
            this.setState({ stattus: response.status });
        }
        else {
            const newMatches = Utils.deleteMatch(this.state.matches, match);
            if (this.mounted) {
                this.setState({ matches: newMatches, loading: false });
            }
        }
    }

    render() {
        let { currentTab, matches, hasMore } = this.state;
        const { currentUser } = this.props;
        let currentMatches;
        if (this.state.status) {
            currentMatches = <ErrorPage status={this.state.status} />;
        }
        else if ((matches.length === 0 && hasMore) || this.state.loading) {
            currentMatches = <Loader />;
        }
        else {
            currentMatches = <HomeMatches matches={matches} hasMore={hasMore} 
                                handleMatchClick={this.handleMatchClick}
                                getMatches={this.getMatches}
                                joinMatch={this.joinMatch}
                                cancelMatch={this.cancelMatch}
                                deleteMatch={this.deleteMatch} />;
        }
        return (
            <Home currentTab={currentTab} handleTabChange={this.handleTabChange}
                    currentUser={currentUser} filters={this.state.filters}
                    updateFilters={this.updateFilters} currentMatches={currentMatches} />
        );
    }

    componentWillUnmount() {
        //TODO stop request
        this.mounted = false;
    }
}

HomeContainer.propTypes = {
    currentUser: PropTypes.string,
    location: PropTypes.object.isRequired,
    history: PropTypes.object.isRequired
}

export default HomeContainer;