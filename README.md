
# Text Adventure Game

## Project Description
This is a **Java-based text adventure game** where players explore a fictional world, solve puzzles, collect items, and face challenges. The game features an inventory system with weight limits and allows players to make decisions that impact the storyline.

### Project Structure:
- **Work Section**: Contains project estimates, sprint plans, a glossary with key terms, and detailed user stories guiding the game's development.
- **Education Section**: Focuses on team strengths, Agile methodologies, and other relevant educational materials integrated into the project.

## Getting Started

### Prerequisites
Before running the game, make sure you have the following installed:
- **Java Development Kit (JDK) 17** or later
- **Apache Maven** (for building the project)
- **Git** (for version control)

#### Installing Git
1. **Download Git**:
   - Visit the [Git website](https://git-scm.com/) and download the latest version of Git for your operating system.
   
2. **Install Git**:
   - Follow the installation instructions provided during the setup.

3. **Verify Installation**:
   ```bash
   git --version
   ```

4. **Add Git to PATH (if not added automatically)**:
   - **Windows**:
     - Search for "Environment Variables" in the Start menu and click "Edit the system environment variables."
     - In the System Properties window, click "Environment Variables."
     - Find the "Path" variable under System Variables, click "Edit," and add the Git `bin` directory (e.g., `C:\Program Files\Git\bin`).
   - **Linux/Mac**:
     - Edit your shell configuration file (e.g., `.bashrc`, `.zshrc`, or `.bash_profile`) and add the following line:
       ```bash
       export PATH=$PATH:/usr/local/git/bin
       ```
     - Save the file and reload the shell configuration:
       ```bash
       source ~/.bashrc
       ```

#### Installing Apache Maven
1. **Download Maven**:
   - Visit the [Maven website](https://maven.apache.org/download.cgi) and download the latest version of Maven.

2. **Extract Maven**:
   - Extract the downloaded archive to a directory on your system, such as `C:\Program Files\Maven` (Windows) or `/usr/local/maven` (Linux/Mac).

3. **Add Maven to PATH**:
   - **Windows**:
     - Follow the same steps as for Git, but add the `bin` directory of your Maven installation (e.g., `C:\Program Files\Maven\bin`).
   - **Linux/Mac**:
     - Edit your shell configuration file (e.g., `.bashrc`, `.zshrc`, or `.bash_profile`) and add the following line:
       ```bash
       export PATH=$PATH:/path/to/maven/bin
       ```
     - Save the file and reload the shell configuration:
       ```bash
       source ~/.bashrc
       ```

4. **Verify Maven Installation**:
   ```bash
   mvn -version
   ```

---

### Installation
1. **Clone the repository** to your local machine:
    ```bash
    git clone https://github.com/pepsilad2222/UnfortunateTeam.git
    cd UnfortunateTeam
    ```

2. **Locate the correct Directory**:
    ```bash
    cd "project work"
    ```
    **then type**
    ```bash
    cd Text Adventure Game
    ```
    
3. **Build the project** using Maven:
    ```bash
    mvn clean install
    ```

4. **Running Tests** using Maven:
    The game includes a suite of tests to ensure functionality. To run these tests, execute:
    ```bash
    mvn test
    ```

5. **Run the game**:
    After the build completes, you can start the game by running this command in the `command line`:
    ```bash
    mvn compile exec:java
    ```

---

### How to Play
- **Objective**: Fight the enemies, Buy new items from the shop, Save the love of your life.
- **Inventory Management**: View your inventory and see whats in it.

---
