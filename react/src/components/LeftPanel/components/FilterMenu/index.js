import React, { Component } from 'react';
import { reduxForm } from 'redux-form';
import PropTypes from 'prop-types';
import FilterMenu from './layout';

class FilterMenuContainer extends Component {

    onSubmit = (values) => {
        this.props.updateFilters({
            "country": values.country,
            "state": values.state,
            "city": values.city,
            "sport": values.sport
        });
    }

    render() {
        const { handleSubmit, submitting, tabs } = this.props; 
        return (
            <FilterMenu handleSubmit={handleSubmit} submitting={submitting}
                        onSubmit={this.onSubmit} tabs={tabs} />
        );
    }
}

FilterMenuContainer.propTypes = {
    updateFilters: PropTypes.func.isRequired
}

FilterMenuContainer = reduxForm({
    form: 'matchFilters',
    destroyOnUnmount: false, // set to true to remove data on refresh
})(FilterMenuContainer);

export default FilterMenuContainer;