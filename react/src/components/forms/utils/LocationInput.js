import React from 'react';
import createRenderer from './CreateRenderer';
import SubLocationInput from './SubLocationInput';

const geolocate = () => {
    //TODO implement to complete other inputs
}

const LocationInput = createRenderer((input, label, inputType) => {
    //TODO enable birthday button
    return (
        <React.Fragment>
            <div className="form-group" id="locationField">
                <input {...input} type={inputType} className="form-control" 
                        id="autocomplete" placeholder="Address" onFocus={geolocate}/> 
            </div>
            <SubLocationInput label="Country" id="country" path="country" />
            <SubLocationInput label="Street" id="route" path="street" />
            <SubLocationInput label="City" id="locality" path="city" />
            <SubLocationInput label="State" id="administrative_area_level_1" path="state" />
        </React.Fragment>
    );
});


export default LocationInput;
