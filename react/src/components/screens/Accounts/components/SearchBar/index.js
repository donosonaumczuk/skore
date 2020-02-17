import React from 'react';
import { reduxForm, Field } from 'redux-form';
import PropTypes from 'prop-types';
import SearchInput from '../../../../panel/filterMenu/SearchInput';
import i18next from 'i18next';

let SearchBar = ({ handleSubmit, submitting, onSubmit, label, inputStyle }) => {
    return (
         <div className="row p-2 mt-2 mb-2">
            <div className="container-fluid">
                <form onSubmit={handleSubmit(onSubmit)}>
                    <Field name="usernames" containerId="searchBar" inputType="text"
                            labelText={label} inputId="searchinput"
                            inputStyle={inputStyle} component={SearchInput} 
                            placeholderText={i18next.t('accounts.searchPlaceholder')}
                            submitting={submitting} />
                </form>
            </div>
        </div>
                    
    );
}

SearchBar.propTypes = {
    handleSubmit: PropTypes.func.isRequired,
    onSubmit: PropTypes.func.isRequired,
    label: PropTypes.string.isRequired,
    inputStyle: PropTypes.string.isRequired
}

SearchBar = reduxForm({
    form: 'userSearch',
    destroyOnUnmount: true,
})(SearchBar);

export default SearchBar;