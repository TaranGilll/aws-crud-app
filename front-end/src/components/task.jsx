import React, { Component } from 'react';

class Task extends Component {
    render() {
        return (
            <div>
                <p style={{color: "#339933"}}> { this.props.task.name } </p>
                <button
                    onClick = { () => this.props.onDelete(this.props.task.id)}
                >
                    Delete
                </button>
                <button
                    onClick = { () => this.props.onEdit(this.props.task )}
                >
                    Edit
                </button>   
            </div>
        );
    }
}

export default Task;