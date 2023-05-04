import React, { Component } from 'react';
import Tasks from './components/tasks';
import './App.css';

class App extends Component {

    constructor(props) {
        super(props);

        this.handleSubmit = this.handleSubmit.bind(this);
        this.handleChange = this.handleChange.bind(this);
        this.handleDelete = this.handleDelete.bind(this);
        this.handleEdit = this.handleEdit.bind(this);
    }

    state = {
      temp: "",
      students: []
    };

    componentDidMount() {
        console.log("GET");
        fetch(AWS_APIGATEWAY_ARN + '/Stage/students', {
          method: 'GET',
          headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'mode': 'no-cors',
          }
        })
        .then(res => res.json())
        .then(data => {
            const newStudents = data.students;
            var result = [];

            for(let currElem of newStudents) {
                let newStdName = currElem.firstName + " " + currElem.lastName;
                const newStd = {id: currElem.studentID, name: newStdName};
                result.push(newStd);
            }
            this.setState( { students: result });
        });
    }

    handleSubmit(e) {
        console.log("POST");
        e.preventDefault();
        const { temp, students } = this.state;
        fetch(AWS_APIGATEWAY_ARN + '/Stage/students', {
            method: 'POST',
            headers: {
              'Accept': 'application/json',
              'Content-Type': 'application/json',
              'mode': 'no-cors'
            },
            body: JSON.stringify({ temp })
        })
        .then(res => res.json())
        .then(data => {
            const newStudent = {id: data.student.studentID, name: data.student.firstName + " " + data.student.lastName};
            this.setState({
                students: [...students, newStudent]
            });
        });
        console.log(this.state.students);
        e.target.reset();

    }

    handleChange(e) {
        this.setState({
            temp: e.target.value
        });
    }
    
    handleDelete ( studentId ) { 
        console.log("DELETE");
        fetch(AWS_APIGATEWAY_ARN + '/Stage/students', {
            method: 'DELETE',
            headers: {
              'Accept': 'application/json',
              'Content-Type': 'application/json',
              'mode': 'no-cors'
            },
            body: JSON.stringify({ studentId })
        });
        const students = this.state.students.filter(s => s.id !== studentId);
        this.setState( { students });
    }


    handleEdit( student ) {
      console.log("PUT");
      const students = [...this.state.students];
      const index = students.indexOf(student);
      students[index] = { ...student };
      students[index].name = this.state.temp;
      var updateVar = student.id + " " + this.state.temp;
      fetch(AWS_APIGATEWAY_ARN + '/Stage/students', {
          method: 'PUT',
          headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'mode': 'no-cors'
          },
          body: JSON.stringify({ updateVar })
      });
      this.setState({ students });
    }

    render() {
        return (<div className="listContainer">
            <div className="listBody" >
              <div className="listHeader"><b>To Do List</b></div>
              <div style={{marginTop: "20px"}}>
                <form onSubmit={ this.handleSubmit } >
                    <input type="text" onChange={this.handleChange} placeholder="Enter task"/>
                    <button className="createTaskBtn">Create Task</button>
                </form>
              </div>
              <div style={{marginTop: "5px"}}>
                <Tasks
                  tasks={this.state.students}
                  onDelete={this.handleDelete}
                  onEdit={this.handleEdit}
                />
              </div>
            </div>
        </div>);
    }
}

export default App;