import React, { Component } from 'react';
import SportService from '../../../services/SportService';
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
            this.setState({ status: response.status });
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
        const { sports, hasMore, status } = this.state;
        const isLoading = sports.length === 0 && hasMore
        return (
            <Sports sports={sports} getSports={this.getSports} hasMore={hasMore}
                    isLoading={isLoading} error={status} />
        );
        
    }

    componentWillUnmount = () => {
        this.mounted = false
    }
}

export default SportsContainer;