# Python Implementation of Lambdas

- `cd` into `src`
- Make a virutual env in the `venv` directory and activate it.
- `pip install -r requirements.txt`
- Call `./mkpkg.sh`
- Upload resulting package.zip to Lambda
- Important: Set Lambda runtime to python3.12, or Pillow won't work.
- Configure Lambda to use the correct entry point: `resizeImage.handle`, for example.
- Modify `callservice.sh` from the root of this repository to point to the lambda.
- Call it, observe changes in S3.