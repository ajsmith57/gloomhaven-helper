import React, { Component } from 'react';
import { connect } from 'react-redux';

class Monster extends Component {

    render() {
        return (
            <li className="">
            </li>
        );
    }
}

const mapStateToProps = (state) => {
    return {};
}

const mapDispatchToProps = (dispatch) => {
    return {
        //UI event functions
    };
}


export default connect(mapStateToProps, mapDispatchToProps)(Monster);