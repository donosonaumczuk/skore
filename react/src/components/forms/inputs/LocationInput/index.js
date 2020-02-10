import React, { Component } from 'react';
import PlacesAutocomplete, { geocodeByAddress } from 'react-places-autocomplete';
import i18next from 'i18next';
import Loader from '../../../Loader';

class LocationInput extends Component {
    mounted = false;
    constructor(props) {
        super(props);
        this.state = { 
                address: '',
                street: '',
                city: '',
                state: '',
                country: '',
                number: ''
        };
    }
   
    handleChange = address => {
        if (this.mounted) {
            this.setState({ address });
        }
    };
    
    loadValueForTypes = (longName, shortName, types) => {
        if (this.mounted && types) {
            types.forEach( type => {
                if (type === 'country') {
                    this.setState({ country: longName ? longName : shortName});
                }
                else if (type === 'administrative_area_level_1') {
                    this.setState({ state: shortName ? shortName : longName})
                }
                else if (type === 'locality' || type === 'sublocality') {
                    this.setState({ city: longName ? longName : shortName});
                }
                else if (type === 'route') {
                    this.setState({ street: shortName ? shortName : longName })
                }
                else if (type === 'street_number') {
                    this.setState({ number: longName ? longName : shortName })
                }
            })
        }
    }

    loadHome = components => {
        if (this.mounted) {
            this.setState({ 
                address: '',
                street: '',
                city: '',
                state: '',
                country: '',
                number: ''
            });
            for(let i = 0; i < components.length; i++) {
                const longName = components[i].long_name;
                const shortName = components[i].short_name;
                const types = components[i].types;
                this.loadValueForTypes(longName, shortName, types);
            }
        }
    }

    handleSelect = async address => {
        try {
            const results  = await geocodeByAddress(address);
            const { updateLocation, changeFieldsValue, touchField } = this.props;
            this.loadHome(results[0].address_components);
            updateLocation(this.state, changeFieldsValue, touchField);
        }
        catch(err) {
            //TODO handle error
        }
        
    };

    componentDidMount() {
        this.mounted = true;
    }
   
    render() {
        return (
            <PlacesAutocomplete value={this.state.address} onChange={this.handleChange}
                            onSelect={this.handleSelect}>
                {({ getInputProps, suggestions, getSuggestionItemProps, loading }) => (
                    <div className="form-group">
                        <div>
                            <label htmlFor='address'>{i18next.t('location.address')}</label> 
                        </div>
                        <input {...getInputProps({ placeholder: i18next.t('location.address'),
                                                    className: 'location-search-input form-control', 
                                                    id: 'address', autoComplete: 'new-password' })} />
                        <div className="autocomplete-dropdown-container">
                        {loading && <Loader />}
                        {suggestions.map(suggestion => {
                            const className = suggestion.active ? 'location-suggestion-active'
                                                                : 'location-suggestion';
                            return (
                                <div {...getSuggestionItemProps(suggestion, { className })}>
                                    <span>{suggestion.description}</span>
                                </div>
                            );
                        })}
                        </div>
                    </div>
                )}
            </PlacesAutocomplete>
        );
    }

    componentWillUnmount() {
        this.mounted = false;
    }
}

export default LocationInput;