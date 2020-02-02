import React, { Component } from 'react';
import SportService from '../../../services/SportService';
import Loader from '../../Loader';
import Utils from './../../utils/Utils';
import Sports from './layout';

const INITIAL_OFFSET = 0;
const QUERY_QUANTITY = 10;

class SportsContainer extends Component {
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
            //TODO handle error
        }
        else if (this.mounted) {
            this.addSports(response);
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
            const { sports, hasMore } = this.state;
            return (
                <Sports sports={sports} getSports={this.getSports} hasMore={hasMore} />
            );
        }

    }

    componentWillUnmount = () => {
        this.mounted = false
    }
}

export default SportsContainer;