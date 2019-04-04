import subprocess
cmdCommand = "sudo shutdown -h now"
process = subprocess.Popen(cmdCommand.split(), stdout=subprocess.PIPE)
