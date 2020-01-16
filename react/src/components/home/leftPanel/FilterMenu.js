import React from 'react';
import i18next from 'i18next';
import FilterTitle from '../../panel/filterMenu/FilterTitle';
import FilterInput from '../../panel/filterMenu/FilterInput';
import AddFilterButton from '../../panel/filterMenu/AddFilterButton';

const applyFilters = () => {
    //TODO make request with filters
}

const FilterMenu = () => {
    const inputStyle = "form-control filter-input mb-2";
    return (
        <div className="row filters p-4 mt-2">
            <div className="container-fluid">
                <FilterTitle title={i18next.t('home.filtersAndCategories')} titleStyle="left-panel-title" />
                <FilterInput containerId="country" labelText={i18next.t('home.countryFilter')}
                            inputStyle={inputStyle} inputId="country-filter" inputType="text" />
                <FilterInput containerId="state" labelText={i18next.t('home.stateFilter')}
                            inputStyle={inputStyle} inputId="state-filter" inputType="text" />
                <FilterInput containerId="city" labelText={i18next.t('home.cityFilter')}
                            inputStyle={inputStyle} inputId="city-filter" inputType="text" />
                <FilterInput containerId="sport" labelText={i18next.t('home.sportFilter')}
                            inputStyle={inputStyle} inputId="sport-filter" inputType="text" />
                <AddFilterButton buttonStyle="btn btn-outline-secondary" handleClick={applyFilters}
                                buttonText={i18next.t('home.addFilters')} />
            </div>
        </div>
    );
}

export default FilterMenu;