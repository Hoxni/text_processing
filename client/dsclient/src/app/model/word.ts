export class Word {
  word_: string;
  frequency: bigint;

  constructor({word, frequency}){
    this.word_ = word;
    this.frequency = frequency;
  }

}
