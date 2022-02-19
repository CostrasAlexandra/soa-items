import * as React from 'react';
import PropTypes from 'prop-types';

export default class Button extends React.Component {

    static propTypes = {
        name: PropTypes.string,
        onClick: PropTypes.func
    }

    static defaultProps = {
        name: ''
    }

    render() {
        const {name, onClick} = this.props;
        return (
            <button className={'btn btn-primary'} onClick={onClick}>{name}</button>
        )
    }
}