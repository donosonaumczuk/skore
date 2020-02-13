import React, { Component } from 'react';
import PropTypes from 'prop-types';
import LoginForm from '../../forms/LogInForm';

function WithAuthentication(WrappedComponent) {
	return class Authentication extends Component {
			constructor(props) {
				super(props);
				let param = null;
				if (props.match) {
					param = props.match.params.param
				}
				this.state = {
					param: param
				}
			}

			getLoginUrl = (url, baseUrl) => {
				let loginUrl = url;
				if (this.state.param && baseUrl) {
					loginUrl = `${baseUrl}/${this.state.param}`;
				}
				return loginUrl;
			}

			render () {
				const { needsAuthentication, url, baseUrl, updateUser, ...props } = this.props;
				if (needsAuthentication) {
					let loginUrl = this.getLoginUrl(url, baseUrl);
					return ( <LoginForm url={loginUrl} updateUser={updateUser} /> );
				}
				else {
					if (this.state.param) {
						return ( <WrappedComponent {...props} param={this.state.param} /> );
					}
					else {
						return ( <WrappedComponent {...props} /> );
					}
				}
			}
		}
	
}

WithAuthentication.propTypes = {
	needsAuthentication: PropTypes.bool.isRequired,
	url: PropTypes.string,
	baseUrl: PropTypes.string,
	match: PropTypes.object,
	updateUser: PropTypes.func.isRequired
}

export default WithAuthentication;