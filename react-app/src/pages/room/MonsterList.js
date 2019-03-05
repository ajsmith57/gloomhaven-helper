import React, {Component} from 'react';
import {connect} from 'react-redux';

import MonsterType from './MonsterType';

import {addMonster} from './../../store/actions/actions';

class MonsterList extends Component {

  constructor(props) {
    super(props);

    this.monsterSelect = React.createRef();
    this.addMonster = this.addMonster.bind(this);
  }

  addMonster() {
    const monsterId = this.monsterSelect.current.value;
    this.props.addMonster(this.props.room.hash, monsterId);
  }

  generateTypes() {
    // Filter out types without any instances
    return this.props.monsters.filter(type =>
      type.monsterInstances.length > 0
    ).map(type =>
      <MonsterType key={type.id} type={type} />
    );
  }

  render() {
    
    const monsterTypes = this.props.monsters.map((type, index) =>
      <option value={type.id} key={index}>{type.name}</option>
    );

    return (
      <div className="monster-list m-2">
        <div className="toolbar columns">
          <div className="control col">
            <div className="select">
              <select className="input" ref={this.monsterSelect}>
                {monsterTypes}
              </select>
            </div>
          </div>
          <div className="control col">
          <div className="level-right">
            <button className="button is-dark themed-font" onClick={this.addMonster}>
              + Add Monster
            </button>
          </div>

        </div>
        </div>
        {this.generateTypes()}
      </div>
    );
  }
}

const mapStateToProps = (state) => {
  return {
    ...state.session
  };
}

const mapDispatchToProps = (dispatch) => {
  return {
    addMonster: (hash, monsterId) => dispatch(addMonster(hash, monsterId))
  };
}


export default connect(mapStateToProps, mapDispatchToProps)(MonsterList);