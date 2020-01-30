import React, { Component } from 'react';
import { Field, reduxForm } from 'redux-form';
import PropTypes from 'prop-types';
import i18next from 'i18next';
import FilterTitle from '../../panel/filterMenu/FilterTitle';
import FilterInput from '../../panel/filterMenu/FilterInput';
import AddFilterButton from '../../panel/filterMenu/AddFilterButton';

class FilterMenu extends Component {

    onSubmit = (values) => {
        this.props.updateFilters({
            "country": values.country,
            "state": values.state,
            "city": values.city,
            "sport": values.sport
        });
    }

    render() {
        const { handleSubmit, submitting } = this.props; 
        const inputStyle = "form-control filter-input mb-2";
        return (
            <div className="row filters p-4 mt-2">
                <div className="container-fluid">
                    <form onSubmit={handleSubmit(this.onSubmit)}>
                        <FilterTitle title={i18next.t('home.filtersAndCategories')} titleStyle="left-panel-title" />
                        <Field name="country" containerId="country" labelText={i18next.t('home.countryFilter')}
                                    inputStyle={inputStyle} inputId="country-filter" inputType="text"
                                    component={FilterInput} />
                        <Field name="state" containerId="state" labelText={i18next.t('home.stateFilter')}
                                    inputStyle={inputStyle} inputId="state-filter" inputType="text"
                                    component={FilterInput}  />
                        <Field name="city" containerId="city" labelText={i18next.t('home.cityFilter')}
                                    inputStyle={inputStyle} inputId="city-filter" inputType="text"
                                    component={FilterInput}  />
                        <Field name="sport" containerId="sport" labelText={i18next.t('home.sportFilter')}
                                    inputStyle={inputStyle} inputId="sport-filter" inputType="text"
                                    component={FilterInput}  />
                        <AddFilterButton buttonStyle="btn btn-outline-secondary" submitting={submitting}
                                        buttonText={i18next.t('home.addFilters')} />
                    </form>
                </div>
            </div>
        );
    }
}

FilterMenu.propTypes = {
    updateFilters: PropTypes.func.isRequired
}

FilterMenu = reduxForm({
    form: 'matchFilters',
    destroyOnUnmount: false, // set to true to remove data on refresh
})(FilterMenu);

export default FilterMenu;