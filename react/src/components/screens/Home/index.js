import React, { Component } from 'react';
import queryString from 'query-string';
import Axios from 'axios';
import PropTypes from 'prop-types';
import Spinner from 'react-spinkit'
import HomeMatches from './components/HomeMatches';
import MatchService from '../../../services/MatchService';
import Utils from '../../utils/Utils';
import Loader from '../../Loader';
import Home from './layout';
import AuthService from '../../../services/AuthService';
import { SC_CLIENT_CLOSED_REQUEST, SC_UNAUTHORIZED, SC_OK } from '../../../services/constants/StatusCodesConstants';

const INITIAL_OFFSET = 0;
const QUERY_QUANTITY = 5;
const MIN_TAB = 0;
const MAX_TAB = 3;
const NO_TAB = 0;
const TO_JOIN_TAB = 1;
const JOINED_TAB = 2;
const CREATED_TAB = 3;

const getCurrentTab = (currentUser, tab) => {
    let currentTab = NO_TAB;
    if (currentUser) {
        if (tab && tab > MIN_TAB && tab <= MAX_TAB) {
            currentTab = parseInt(tab);
        }
        else {
            currentTab = TO_JOIN_TAB;
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
            anonymous: false,
            currentMatch: null,
            currentSource: null
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
            if (response.status !== SC_CLIENT_CLOSED_REQUEST) {
                this.setState({ status: response.status });
            }
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

    cancelRequestIfPending = () => {
        if (this.state.currentSource) {
            this.state.currentSource.cancel();
            if (this.mounted) {
                this.setState({ currentSource: null });
            }
        }
    }

    getSourceToken = () => {
        const newSource = Axios.CancelToken.source();
        if (this.mounted) {
            this.setState({ currentSource: newSource });
        }
        return newSource.token;
    }

    getMatches = async () => {
        let response;
        const { currentUser } = this.props;
        const { offset, total, currentTab, filters } = this.state;
        const newFilters = Utils.removeUnknownHomeFilters(filters);
        this.cancelRequestIfPending();
        const token = this.getSourceToken();
        if (currentTab === NO_TAB) {
            response = await MatchService.getMatches(offset, total, newFilters, token);
        }
        else if (currentTab === TO_JOIN_TAB) {
            response = await MatchService.getMatchesToJoin(currentUser, offset, total, newFilters, token);
        }
        else if (currentTab === JOINED_TAB) {
            response = await MatchService.getMatchesJoinedBy(currentUser, offset, total, newFilters, token);
        }
        else if (currentTab === CREATED_TAB) {
            response = await MatchService.getMatchesCreatedBy(currentUser, offset, total, newFilters, token);
        }
        if (this.mounted) {
            this.updateMatches(response);
        }
    }

    joinMatch = (e, match) => {
        e.stopPropagation();
        if (this.props.currentUser) {
            const userId = AuthService.getUserId();
            this.joinMatchLogged(match, userId);
        }
        else {
            this.joinMatchAnonymous(match);
        }
    }

    joinMatchLogged = async (match, userId) => {
        if (this.mounted) {
            this.setState({ executing: true });
        }
        const response = await MatchService.joinMatchWithAccount(match.key, userId);
        if (response.status) {
            if (response.status === SC_UNAUTHORIZED) {
                const status = AuthService.internalLogout();
                if (status === SC_OK) {
                    this.props.history.push(`/login`);
                }
                else {
                    this.setState({ error: status });
                }
            }
            else if (this.mounted) {
                //TODO handle 409 already joined
                this.setState({ error: response.status, executing: false });
            }
        }
        else {
            const newMatches = Utils.replaceWithNewMatch(this.state.matches, match);
            if (this.mounted) {
                this.setState({ matches: newMatches, executing: false });
            }
            this.props.history.push(`/match/${match.key}`);
        }
    }

    joinMatchAnonymous = (match) => {
        if (match.competitive && this.mounted) {
            this.props.history.push(`/authenticatedJoin/${match.key}`);
        }
        else if ( this.mounted) {
            this.props.history.push(`/?matchKey=${match.key}`);
            this.setState({ anonymous: true, currentMatch: match });
        }
    }
    
    cancelMatch = (e, match) => {
        e.stopPropagation();
        if (this.props.currentUser) {
            const userId = AuthService.getUserId();
            this.cancelMatchLogged(match, userId);
        }
        else {
            this.setStatus({ status: SC_UNAUTHORIZED });
        }
    }

    cancelMatchLogged = async (match, userId) => {
        if (this.mounted) {
            this.setState({ executing: true });
        }
        const response = await MatchService.cancelMatchWithAccount(match.key, userId);
        if (response.status) {
            if (response.status === SC_UNAUTHORIZED) {
                const status = AuthService.internalLogout();
                if (status === SC_OK) {
                    this.props.history.push(`/login`);
                }
                else {
                    this.setState({ error: status });
                }
            }
            else if (this.mounted) {
                //TODO handle 409 already cancelled asistance
                this.setState({ error: response.status, executing: false });
            }
        }
        else {
            const newMatches = Utils.replaceWithNewMatch(this.state.matches, match);
            if (this.mounted) {
                this.setState({ matches: newMatches, executing: false });
            }
            this.handleTabChange(TO_JOIN_TAB);
        }
    }
    
    deleteMatch = async (e, match) => {
        e.stopPropagation();
        if (this.mounted) {
            this.setState({ executing: true });
        }
        const response = await MatchService.deleteMatch(match.key);
        if (response.status && this.mounted) {
            this.setState({ status: response.status });
        }
        else {
            const newMatches = Utils.deleteMatch(this.state.matches, match);
            if (this.mounted) {
                this.setState({ matches: newMatches, executing: false });
            }
        }
    }

    static getDerivedStateFromProps(nextProps, prevState) {
        const queryParams = queryString.parse(nextProps.location.search);
        const matchKey = queryParams.matchKey;
        if (prevState.currentMatch && !matchKey) {
            return { 
                ...prevState,
                currentMatch: null,
                anonymous: false
            };
        }
        else return null;
    }

    render() {
        let { currentTab, matches, hasMore } = this.state;
        const { currentUser } = this.props;
        let currentMatches;
        if (this.state.executing) {
            currentMatches = <Spinner name="ball-spin-fade-loader" /> //TODO center and hoc
        }
        else if (matches.length === 0 && hasMore) {
            currentMatches = <Loader />;//TODO hoc
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
                    updateFilters={this.updateFilters} currentMatches={currentMatches}
                    anonymous={this.state.anonymous} error={this.state.status} 
                    currentMatch={this.state.currentMatch} />
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