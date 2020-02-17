import React, { Component } from 'react';
import { reduxForm } from 'redux-form';
import PropTypes from 'prop-types';
import FilterMenu from './layout';

class FilterMenuContainer extends Component {

    onSubmit = (values) => {
        let onlyLikedSports = values.onlyLikedSports ? values.onlyLikedSports : "false";
        let onlyLikedUsers = values.onlyLikedUsers ? values.onlyLikedUsers : "false";
        this.props.updateFilters({
            "country": values.country,
            "state": values.state,
            "city": values.city,
            "sport": values.sport,
            "onlyLikedSports": `${onlyLikedSports}`,
            onlyLikedUsers: `${onlyLikedUsers}`
        });
    }

    render() {
        const { handleSubmit, submitting, tabs, currentUser } = this.props;
        return (
            <FilterMenu handleSubmit={handleSubmit} submitting={submitting}
                        onSubmit={this.onSubmit} tabs={tabs}
                        currentUser={currentUser} />
        );
    }
}

FilterMenuContainer.propTypes = {
    updateFilters: PropTypes.func.isRequired,
    currentUser: PropTypes.string
}

FilterMenuContainer = reduxForm({
    form: 'matchFilters',
    destroyOnUnmount: false, // set to true to remove data on refresh
})(FilterMenuContainer);

export default FilterMenuContainer;
