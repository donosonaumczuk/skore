import React, { Component } from 'react';
import { reduxForm, Field } from 'redux-form';
import PropTypes from 'prop-types';
import FilterMenu from './layout';

let SearchBar = ({ handleSubmit, submitting }) => {
    return (
         <div className="row filters p-4 mt-2">
            <div className="container-fluid">
                <form onSubmit={handleSubmit(onSubmit)}>
                    <Field name="username" containerId="usernameSearch" inputType="text"
                            labelText={i18next.t('createUserForm.username')}
                            inputStyle={inputStyle} inputId="country-filter" 
                            placeholderText="Search..." component={FilterInput} />
                    <AddFilterButton buttonStyle="btn btn-outline-secondary" submitting={submitting}
                                    buttonText={i18next.t('home.addFilters')} />
                </form>
            </div>
        </div>
                    
    );
}

SearchBar.propTypes = {
    onSubmit: PropTypes.func.isRequired
}

SearchBar = reduxForm({
    form: 'userSearch',
    destroyOnUnmount: true,
})(SearchBar);

export default SearchBar;