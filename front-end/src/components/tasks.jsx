import React, { Component } from 'react';
import Task from './task'

class Tasks extends Component {    
    render() {
        return (
            <div>
                { this.props.tasks.map(task => (
                    <Task
                        key={task.id}
                        onDelete={this.props.onDelete}
                        onEdit={this.props.onEdit}
                        task={task}
                    />
                ))}
            </div>
        )
    }
}

export default Tasks;