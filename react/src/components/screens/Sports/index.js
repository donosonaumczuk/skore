import React, { Component } from 'react';
import PropTypes from 'prop-types';
import SportService from '../../../services/SportService';
import UserService from '../../../services/UserService';
import Utils from './../../utils/Utils';
import Sports from './layout';
import { SC_CONFLICT } from '../../../services/constants/StatusCodesConstants';

const INITIAL_OFFSET = 0;
const QUERY_QUANTITY = 10;
const LIKES_OFFSET = 0;
const LIKES_LIMIT = 100;

class SportsContainer extends Component {
    mounted = false;
    constructor(props) {
        super(props);
        const hasMoreLikes = this.props.currentUser ? true : false;
        this.state = {
            sports: [],
            offset: INITIAL_OFFSET,
            total: QUERY_QUANTITY,
            hasMore: true,
            likes: {},
            likesOffset: LIKES_OFFSET,
            likesLimit: LIKES_LIMIT,
            hasMoreLikes: hasMoreLikes
        }
    }

    updateLikes = likedSports => {
        let newLikes = { ...this.state.likes };
        likedSports.forEach(likedSport => {
            newLikes[likedSport.sportName] = true;
        });
        return newLikes;
    }

    getLikes = async () => {
        const { likesOffset, likesLimit } = this.state;
        const username = this.props.currentUser;
        if (this.mounted) {
            this.setState({ executing: true });
        }
        let response = await UserService.getLikedSports(username, likesOffset, likesLimit);
        if (response.status) {
            if (this.mounted) {
                this.setState({ status: response.status, hasMoreLikes: false });
            }
        }
        else if (this.mounted) {
            const hasMore = Utils.hasMorePages(response.links);
            const newLikes = this.updateLikes(response.likedSports);
            this.setState({
                likes: newLikes,
                likesOffset: this.state.likesOffset + response.likedSports.length,
                hasMoreLikes: hasMore,
                executing: false
            });
        }
    }

    addSports = response => {
        const  { sports } = response;
        const hasMore = Utils.hasMorePages(response.links);
        this.setState({
            sports: [...this.state.sports, ...sports],
            offset: this.state.offset + sports.length,
            total: QUERY_QUANTITY,
            hasMore: hasMore
        })
    }

    getSports = async () => {
        const response = await SportService.getSports();
        if (response.status) {
            this.setState({ status: response.status });
        }
        else if (this.mounted) {
            this.addSports(response);
        }
    }

    likeSport = async (e, sportName) => {
        e.stopPropagation();
        const { currentUser } = this.props;
        if (this.mounted) {
            this.setState({ executing: true });
        }
        const response = await UserService.likeSport(currentUser, sportName);
        if (response.status && response.status !== SC_CONFLICT) {
            if (this.mounted) {
                this.setState({ error: response.status, executing: false });
            }
        }
        else if (this.mounted) {
            const newLikes = { ...this.state.likes};
            newLikes[sportName] = true;
            this.setState({ likes: newLikes, executing: false });
        }
    }

    dislikeSport = async (e, sportName) => {
        e.stopPropagation();
        const { currentUser } = this.props;
        if (this.mounted) {
            this.setState({ executing: true });
        }
        const response = await UserService.dislikeSport(currentUser, sportName);
        if (response.status && response.status !== SC_CONFLICT) {
            if (this.mounted) {
                this.setState({ error: response.status, executing: false });
            }
        }
        else if (this.mounted) {
            const newLikes = { ...this.state.likes};
            newLikes[sportName] = false;
            this.setState({ likes: newLikes, executing: false });
        }
    }

    componentDidMount = async () => {
        this.mounted = true;
        this.getSports();
        if (this.props.currentUser) {
            let hasMore = this.state.hasMoreLikes;
            while (hasMore) {
                if (!this.state.loading) {
                    await this.getLikes();
                    hasMore = this.mounted ? this.state.hasMoreLikes : false;
                }
            }
        }
    }

    render() {
        const { sports, hasMore, status, likes, hasMoreLikes, executing } = this.state;
        const isLoading = ((sports.length === 0 && hasMore) || hasMoreLikes)
        return (
            <Sports sports={sports} getSports={this.getSports} hasMore={hasMore}
                    currentUser={this.props.currentUser} likes={likes}
                    likeSport={this.likeSport} dislikeSport={this.dislikeSport}
                    isLoading={isLoading} error={status} 
                    isExecuting={executing} />
        );
        
    }

    componentWillUnmount = () => {
        this.mounted = false
    }
}

SportsContainer.propTypes = {
    currentUser: PropTypes.string
}

export default SportsContainer;