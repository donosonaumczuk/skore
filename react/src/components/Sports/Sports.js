import React, { Component } from 'react';
import InfiniteScroll from 'react-infinite-scroll-component';
import Sport from './Sport';
import SportService from '../../services/SportService';
import Loader from '../Loader';

const INITIAL_OFFSET = 0;
const QUERY_QUANTITY = 10;
class Sports extends Component {
    mounted = false;
    constructor(props) {
        super(props);
        this.state = {
            sports: [],
            offset: INITIAL_OFFSET,
            total: QUERY_QUANTITY,
            hasMore: true
        }
    }

    addSports = sports => {
        this.setState({
            sports: [...this.state.sports, ...sports],
            offset: this.state.offset + sports.length,
            total: QUERY_QUANTITY,
            hasMore: true
        })
    }

    getSports = async () => {
        const sports = await SportService.getSports();
        if (sports.status) {
            //TODO handle error
        }
        else if (this.mounted) {
            this.addSports(sports);
        }
    }

    componentDidMount = async () => {
        this.mounted = true;
        this.getSports();
    }

    render() {
        if (this.state.sports.length === 0 && this.state.hasMore) {
            return <Loader />;
        }
        else {
            return (
                <div className="match-container container-fluid">
                    {/* TODO sport Filter */}
                    <InfiniteScroll dataLength={this.state.sports.length} next={this.getSports}
                                    hasMore={this.state.hasMore} loader={<Loader />}>
                        {this.state.sports.map((sport) => <Sport key={sport.sportName} sport={sport} />)}
                    </InfiniteScroll>
                </div>
            );
        }

    }

    componentWillUnmount = () => {
        this.mounted = false
    }
}

export default Sports;