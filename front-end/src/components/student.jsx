import React, { Component } from 'react';

class Student extends Component {
    render() {
        return (
            <div>
                <p style={{color: "#339933"}}> { this.props.student.name } </p>
                <button
                    onClick = { () => this.props.onDelete(this.props.student.id)}
                >
                    Delete
                </button>
                <button
                    onClick = { () => this.props.onEdit(this.props.student )}
                >
                    Edit
                </button>   
            </div>
        );
    }
}

export default Student;