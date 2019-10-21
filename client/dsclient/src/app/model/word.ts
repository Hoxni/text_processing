export class Word {
  word: string;
  frequency: bigint;
  tags: [];

  constructor({word, frequency, tags}){
    this.word = word;
    this.frequency = frequency;
    this.tags = tags;
  }

}
