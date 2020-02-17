import React, {Fragment} from 'react';
import i18next from 'i18next';
import PropTypes from 'prop-types';
import { Field } from 'redux-form';
import FilterTitle from '../../../panel/filterMenu/FilterTitle';
import FilterInput from '../../../panel/filterMenu/FilterInput';
import AddFilterButton from '../../../panel/filterMenu/AddFilterButton';
import FilterCheckbox from '../../../panel/filterMenu/FilterCheckbox';

const getCheckboxTags = currentUser => {
    if (currentUser) {
        return (
            <Fragment>
                <Field name="onlyLikedSports" containerId="onlyLikedSports" inputType="checkbox"
                        labelText={i18next.t('home.onlyLikedSports')} inputId="liked-sports-filter"
                        component={FilterCheckbox} inputStyle="filter-input" />
                <Field name="onlyLikedUsers" containerId="onlyLikedUsers" inputType="checkbox"
                        labelText={i18next.t('home.onlyLikedUsers')} inputId="liked-users-filter"
                        component={FilterCheckbox} inputStyle="filter-input" />
            </Fragment>
        );
    }
    return <Fragment/>
}

const FilterMenu = ({ handleSubmit, submitting, onSubmit, tabs, currentUser }) => {
    const checkboxTags = getCheckboxTags(currentUser);
    const inputStyle = "form-control filter-input mb-2";
    return (
        <div className="row filters p-4 mt-2">
            <div className="container-fluid">
                <form onSubmit={handleSubmit(onSubmit)}>
                    <FilterTitle title={i18next.t('home.filtersAndCategories')} titleStyle="left-panel-title" />
                    {tabs}
                    {checkboxTags}
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

FilterMenu.propTypes = {
    handleSubmit: PropTypes.func.isRequired,
    onSubmit: PropTypes.func.isRequired
}

export default FilterMenu;