import * as React from 'react';
import PropTypes from 'prop-types';

export default class List extends React.Component {

    static propTypes = {
        items: PropTypes.string
    }

    static defaultProps = {
        items: ''
    }

    render() {
        const items = this.props.items.split(',');
        return (
            <ul className="list-group">
                {
                    items.map(item => (<li key={item} className="list-group-item">{item}</li>))
                }
            </ul>
        )
    }
}