import React, { Component } from 'react';
import Student from './student'

class Students extends Component {    
    render() {
        return (
            <div>
                { this.props.students.map(student => (
                    <Student
                        key={student.id}
                        onDelete={this.props.onDelete}
                        onEdit={this.props.onEdit}
                        student={student}
                    />
                ))}
            </div>
        )
    }
}

export default Students;