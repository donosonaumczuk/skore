import React from 'react';
import createRenderer from './CreateRenderer';
import SubLocationInput from './SubLocationInput';
import i18next from 'i18next';

const geolocate = () => {
   //TODO implement
}

const LocationInput = createRenderer((input, label, inputType) => {
    //TODO use i18n and make autocomplete work
    return (
        <React.Fragment>
            <div className="form-group" id="locationField">
                <input {...input} type={inputType} className="form-control" 
                        id="autocomplete" placeholder="Address" onFocus={geolocate}/> 
            </div>
            <SubLocationInput label={i18next.t('createUserForm.country')} id="country" path="country" />
            <SubLocationInput label={i18next.t('createUserForm.street')} id="route" path="street" />
            <SubLocationInput label={i18next.t('createUserForm.city')} id="locality" path="city" />
            <SubLocationInput label={i18next.t('createUserForm.state')} id="administrative_area_level_1" path="state" />
        </React.Fragment>
    );
});


export default LocationInput;
