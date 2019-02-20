import React, {Component} from 'react';
import {connect} from 'react-redux';
import {deleteMonster} from "../../store/actions/actions";

class Monster extends Component {

  constructor(props) {
    super(props);

    this.deleteMonster = this.deleteMonster.bind(this);
  }

  deleteMonster() {
    this.props.deleteMonster(this.props.instance);
  }

  render() {
    return (
      <>
        <div className="title themed-font has-text-light is-size-4">
          {this.props.firstElement ? this.props.instance.monster.name : ''}
        </div>
        <li className="columns" key={this.props.key}>
          <div className="column has-text-light">Encountered #: {this.props.id}</div>
          <div className="column has-text-light"> HP: <input className="input input-short" type="number" defaultValue={this.props.instance.currentHealth}
                                             max={this.props.instance.maxHealth} min="0"/>   
              &nbsp;/&nbsp;{this.props.instance.maxHealth}
          </div>
          <div className="column">
            <button type="button" className="button is-dark themed-font" onClick={this.deleteMonster}>X</button>
          </div>
        </li>
      </>
    );
  }
}

const mapStateToProps = (state) => {
  return {};
}

const mapDispatchToProps = (dispatch) => {
  return {
    deleteMonster: (monster) => dispatch(deleteMonster(monster))
  };
}


export default connect(mapStateToProps, mapDispatchToProps)(Monster);